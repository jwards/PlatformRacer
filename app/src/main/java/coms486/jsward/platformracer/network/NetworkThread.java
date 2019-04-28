package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.PriorityQueue;

import coms486.jsward.platformracer.GameActivity;
import coms486.jsward.platformracer.User;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.network.CreateGamePacket;
import jsward.platformracer.common.network.JoinGamePacket;
import jsward.platformracer.common.network.LobbyPacket;
import jsward.platformracer.common.network.ReqType;
import jsward.platformracer.common.network.StartGamePacket;
import jsward.platformracer.common.network.Status;


import static jsward.platformracer.common.util.Constants.SERVER_PORT;

public class NetworkThread extends Thread {

    private static final String DEBUG_TAG = "NETWORK_THREAD";

    //desktop
    //private static final String SERVER_ADDR  = "desktop-93rq231.student.iastate.edu";
    //laptop
    private static final String SERVER_ADDR  = "desktop-rqgu2tp.student.iastate.edu";

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private GameCommunicationThread gameCommunicationThread;

    private GameActivity gameActivity;
    private GameCore gameCore;

    private boolean connectionAlive;
    private boolean inGame;

    private PriorityQueue<NetRequest> requestQueue;

    public NetworkThread() {
        super();
        requestQueue = new PriorityQueue<>();
        connectionAlive = false;
        inGame = false;
    }

    @Override
    public synchronized void run() {
        Log.d(DEBUG_TAG,"Network thread runnning...");
        initNetwork();

        long nextWait = 0;

        //lobby communication
        while(connectionAlive){
            try {
                Log.d(DEBUG_TAG, "Waiting for "+nextWait+" ms. Or until new request...");

                wait(nextWait);

                NetRequest request;
                synchronized (requestQueue) {
                    if (requestQueue.peek() != null) {
                        if (requestQueue.peek().isReady()) {
                            request = requestQueue.poll();
                            Log.d(DEBUG_TAG, "Executing request: " + request.toString());

                            //tell the request it is being executed
                            request.onExecute();

                            //check if the request should be added back into the queue
                            if (!request.isExpired()) {
                                requestQueue.add(request);
                                Log.d(DEBUG_TAG, "Adding event: " + request.toString() + " back into queue");
                            }

                            //if the queue isn't empty, set the next wait time to the next event;
                            if (requestQueue.peek() != null) {
                                nextWait = requestQueue.peek().getWaitTime();
                                Log.d(DEBUG_TAG, "Next event: "+requestQueue.peek().toString()+" in ... " + nextWait);
                            }

                            switch (request.getType()) {
                                case REQ_JOIN:
                                    sendJoinRequest((SimpleRequest) request);
                                    break;
                                case REQ_CREATE:
                                    sendCreateRequest((SimpleRequest) request);
                                    break;
                                case REQ_LOBBY_LIST:
                                    sendLobbyRequest((LobbyUpdateRequest) request);
                                    break;
                                case REQ_DESTROY:
                                    sendLeaveRequest((SimpleRequest) request);
                                    break;
                                case REQ_START:
                                    sendStartRequest((SimpleRequest) request);
                                    break;
                            }
                        } else{
                            nextWait = requestQueue.peek().getWaitTime();
                            Log.d(DEBUG_TAG, "Next event: "+requestQueue.peek().toString()+" in ... " + nextWait);
                        }
                    } else {
                        //there is nothing in the queue so there is nothing to do, we wait forever.
                        //when a request is added to the queue, notify will be called which will then set nextWait to the
                        //appropriate time if the request is recurring.
                        nextWait = 0;
                    }
                }

                if(inGame){
                    //wait for gameCore to be set
                    while(gameCore == null){
                        wait(250);
                    }

                    beginGame(gameCore);

                    //game has ended
                    inGame = false;

                }


            } catch (InterruptedException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
                //try to reconnect
                closeNetwork();
                initNetwork();
            } catch (ClassNotFoundException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            }
        }
    }

    //called from seperate thread
    //returns true if request can be made
    public synchronized boolean request(NetRequest req){
        if(connectionAlive){
            requestQueue.add(req);
            Log.d(DEBUG_TAG, "Request added to queue " + req.toString());
            Log.d(DEBUG_TAG, requestQueue.toString());
            notify();
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean cancleRequest(String requestId){
        for(NetRequest nr:requestQueue){
            if(nr.getRequestId().equals(requestId)){
                requestQueue.remove(nr);
                return true;
            }
        }
        return false;
    }

    public synchronized void cancleAll(){
        requestQueue.clear();
    }

    public synchronized void setGameCore(GameCore gameCore){
        this.gameCore = gameCore;
        notify();
    }


    public boolean isConnected(){
        return connectionAlive;
    }

    //begins sending and requesting game updates
    //will block until the game is done
    private void beginGame(GameCore gameCore) throws IOException, InterruptedException {
        gameCommunicationThread = new GameCommunicationThread(objectInputStream,objectOutputStream, gameCore);

        //receive game init info
        //this loads the player and player controller data into the local gamecore
        gameCommunicationThread.getGameInfo();

        //tell the activity that we got the player info
        gameActivity.onGameCoreInit();

        //don't keep the reference to the activity
        gameActivity = null;

        //begin game communication
        gameCommunicationThread.start();

        gameCommunicationThread.join();
    }


    private void sendLobbyRequest(LobbyUpdateRequest req) throws IOException, ClassNotFoundException {
        //send request for lobby list
        Log.d(DEBUG_TAG, "Sending lobby update request...");
        objectOutputStream.writeObject(new LobbyPacket(null,req.getLobbyId()));
        getAndHandleResponse(req);
    }

    private void onLobbyResponse(LobbyPacket response, LobbyUpdateRequest request) {
        Log.d(DEBUG_TAG, "Received: " + response.toString());

        if (response.getLobbyId() == -1) {
            //regular lobby list update
            request.getCallback().onLobbyUpdateReceived(response.getInfos());
        } else if (response.getInfos().size() == 0) {
            //requested single lobby but couldn't find a lobby with matching id
            request.getCallback().onSingleLobbyUpdateReceived(null);
        } else {
            request.getCallback().onSingleLobbyUpdateReceived(response.getInfos().get(0));
        }
    }

    private void sendJoinRequest(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send request to join a game
        JoinGamePacket jgp = new JoinGamePacket(req.getExtra());
        Log.d(DEBUG_TAG,"Sending join game request: "+jgp.toString());
        objectOutputStream.writeUnshared(jgp);

        Log.d(DEBUG_TAG, "Waiting for repsonse...");
        getAndHandleResponse(req);
    }

    private void onJoinResponse(JoinGamePacket response,SimpleRequest request){
        Log.d(DEBUG_TAG, "Received: " + response.status);
        request.getCallback().onResponse(ReqType.REQ_JOIN, response.status,response.gameSessionId);
    }

    private void sendLeaveRequest(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send join packet with id -1
        JoinGamePacket jgp = new JoinGamePacket(req.getExtra());
        Log.d(DEBUG_TAG,"Sending leave game request: "+jgp.toString());
        objectOutputStream.writeUnshared(jgp);
        Log.d(DEBUG_TAG, "Waiting for response...");
        getAndHandleResponse(req);
    }

    private void onLeaveResponse(JoinGamePacket response,SimpleRequest request){
        Log.d(DEBUG_TAG, "Recieved: " + response.status);
        request.getCallback().onResponse(ReqType.REQ_DESTROY,response.status,response.gameSessionId);
    }

    private void sendCreateRequest(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send request to create game
        CreateGamePacket cgp = new CreateGamePacket();
        Log.d(DEBUG_TAG,"Sending create game request...");
        objectOutputStream.writeUnshared(cgp);
        getAndHandleResponse(req);
    }

    private void onCreateResponse(CreateGamePacket response,SimpleRequest request){
        Log.d(DEBUG_TAG, "Recieved: " + response.status);
        request.getCallback().onResponse(ReqType.REQ_CREATE,response.status,response.gameSessionId);
    }

    private void sendStartRequest(SimpleRequest req) throws IOException, ClassNotFoundException {
        StartGamePacket sgp = new StartGamePacket();
        Log.d(DEBUG_TAG, "Sending start game request...");
        objectOutputStream.writeUnshared(sgp);
        getAndHandleResponse(req);
    }

    //this is called when the server tells the client to start the game
    private void onStartResponse(StartGamePacket response,LobbyUpdateRequest request){
        Log.d(DEBUG_TAG,"Received: "+response.status);
        if(response.status == Status.OK) {
            //tell main thead game has begun
            request.getCallback().onGameStart();

            //begin game communication
            inGame = true;
        }
    }

    //this is called in response to a request to start the game
    //The game doesn't actually start until a separate response from the server
    private void onStartResponse(StartGamePacket response,SimpleRequest request){
        Log.d(DEBUG_TAG,"Received: "+response.status + " in response to: "+ReqType.REQ_START);
        Log.d(DEBUG_TAG, "Game will start shortly...");
    }

    private void getAndHandleResponse(NetRequest request) throws IOException, ClassNotFoundException {
        Object obj = objectInputStream.readUnshared();

        if (obj instanceof LobbyPacket) {
            onLobbyResponse((LobbyPacket) obj,(LobbyUpdateRequest) request);
        } else if (obj instanceof JoinGamePacket) {
            JoinGamePacket jgp = (JoinGamePacket) obj;
            if(jgp.gameSessionId == -1){
                //leave game response
                onLeaveResponse(jgp,(SimpleRequest)request);
            } else {
                //join game response
                onJoinResponse(jgp,(SimpleRequest)request);
            }
        } else if (obj instanceof CreateGamePacket) {
            onCreateResponse((CreateGamePacket) obj,(SimpleRequest)request);
        } else if (obj instanceof StartGamePacket) {
            if(request instanceof LobbyUpdateRequest){
                onStartResponse((StartGamePacket)obj,(LobbyUpdateRequest) request);
            } else {
                onStartResponse((StartGamePacket) obj, (SimpleRequest) request);
            }
        }

    }


    private void initNetwork(){
        try{
            Log.d(DEBUG_TAG, "Attempting to connect to " + SERVER_ADDR + ":" + SERVER_PORT);
            socket = new Socket();
            socket.connect(new InetSocketAddress(SERVER_ADDR,SERVER_PORT),1000);
            if(socket.isConnected()) {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.flush();
                //every time we connect, we let the server know who we are
                objectOutputStream.writeUnshared(User.USER_ID);
                objectOutputStream.flush();

                Log.d(DEBUG_TAG, "Connection successful");
                connectionAlive = true;
            } else {

            }
        } catch (IOException e){
            Log.d(DEBUG_TAG, "Error connecting to " + SERVER_ADDR + ":" + SERVER_PORT);
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
    }

    private void closeNetwork(){
        //close the socket and its streams
        connectionAlive = false;
        try {
            if(objectOutputStream != null) objectOutputStream.close();
        }catch (IOException e){
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
        try {
            if(objectInputStream != null) objectInputStream.close();
        }catch (IOException e){
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
        try {
            if(socket != null) socket.close();
        }catch (IOException e){
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
    }

    public void setGameInitCallback(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }
}
