package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.PriorityQueue;

import coms486.jsward.platformracer.User;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.network.CreateGamePacket;
import jsward.platformracer.common.network.JoinGamePacket;
import jsward.platformracer.common.network.LobbyPacket;
import jsward.platformracer.common.network.ReqType;
import jsward.platformracer.common.network.StartGamePacket;
import jsward.platformracer.common.network.Status;

import static jsward.platformracer.common.util.Constants.CLIENT_INPUT_POLL_RATE;
import static jsward.platformracer.common.util.Constants.SERVER_PORT;
import static jsward.platformracer.common.util.Constants.SERVER_UPDATE_RATE;

public class NetworkThread extends Thread {

    private static final String DEBUG_TAG = "NETWORK_THREAD";

    //desktop
    private static final String SERVER_ADDR  = "desktop-93rq231.student.iastate.edu";
    //laptop
    //private static final String SERVER_ADDR  = "desktop-rqgu2tp.student.iastate.edu";

    private NetworkManager callback;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private GameReceiveThread gameReceiveThread;
    private GameSendThread gameSendThread;

    private boolean connectionAlive;

    private PriorityQueue<NetRequest> requestQueue;

    public NetworkThread(NetworkManager callback) {
        super();
        this.callback = callback;
        requestQueue = new PriorityQueue<>();
        connectionAlive = false;
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
                                    joinReq((SimpleRequest) request);
                                    break;
                                case REQ_CREATE:
                                    createReq((SimpleRequest) request);
                                    break;
                                case REQ_LOBBY_LIST:
                                    lobbyReq((LobbyUpdateRequest) request);
                                    break;
                                case REQ_DESTROY:
                                    leaveReq((SimpleRequest) request);
                                    break;
                                case REQ_START:
                                    startReq((SimpleRequest) request);
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


    public boolean isConnected(){
        return connectionAlive;
    }


    private void lobbyReq(LobbyUpdateRequest req) throws IOException, ClassNotFoundException {
        //send request for lobby list
        Log.d(DEBUG_TAG, "Sending lobby update request...");
        objectOutputStream.writeObject(new LobbyPacket(null,req.getLobbyId()));

        Object obj = objectInputStream.readUnshared();

        if(obj != null){
            if(obj instanceof LobbyPacket){
                LobbyPacket lp = (LobbyPacket) obj;
                Log.d(DEBUG_TAG,"Received: "+lp.toString());

                if(lp.getLobbyId() == -1){
                    //regular lobby list update
                    req.getCallback().onLobbyUpdateReceived(lp.getInfos());
                } else if(lp.getLobbyId() == -2){
                    //the game has been started
                    req.getCallback().onGameStart();
                } else if(lp.getInfos().size() == 0){
                    //requested single lobby but couldn't find a lobby with matching id
                    req.getCallback().onSingleLobbyUpdateReceived(null);
                } else {
                    req.getCallback().onSingleLobbyUpdateReceived(lp.getInfos().get(0));
                }
            } else {
                Log.d(DEBUG_TAG,"Error receiving LobbyUpdatePacket. Wrong class: "+obj.toString());
            }
        } else {
            Log.d(DEBUG_TAG,"Error receiving LobbyUpdatePacket. Null class");
        }
    }

    private void joinReq(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send request to join a game
        JoinGamePacket jgp = new JoinGamePacket(req.getExtra());
        Log.d(DEBUG_TAG,"Sending join game request: "+jgp.toString());
        objectOutputStream.writeUnshared(jgp);

        Log.d(DEBUG_TAG, "Waiting for repsonse...");
        JoinGamePacket response = (JoinGamePacket) objectInputStream.readUnshared();
        Log.d(DEBUG_TAG, "Received: " + response.status);
        req.getCallback().onResponse(ReqType.REQ_JOIN, response.status,response.gameSessionId);
    }

    private void leaveReq(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send join packet with id -1
        JoinGamePacket jgp = new JoinGamePacket(req.getExtra());
        Log.d(DEBUG_TAG,"Sending leave game request: "+jgp.toString());
        objectOutputStream.writeUnshared(jgp);

        Log.d(DEBUG_TAG, "Waiting for response...");
        JoinGamePacket response = (JoinGamePacket) objectInputStream.readUnshared();
        Log.d(DEBUG_TAG, "Recieved: " + response.status);
        req.getCallback().onResponse(ReqType.REQ_DESTROY,response.status,response.gameSessionId);

    }

    private void createReq(SimpleRequest req) throws IOException, ClassNotFoundException {
        //send request to create game
        CreateGamePacket cgp = new CreateGamePacket();
        Log.d(DEBUG_TAG,"Sending create game request...");
        objectOutputStream.writeUnshared(cgp);

        JoinGamePacket response = (JoinGamePacket) objectInputStream.readUnshared();
        Log.d(DEBUG_TAG, "Recieved: " + response.status);
        req.getCallback().onResponse(ReqType.REQ_CREATE,response.status,response.gameSessionId);
    }

    private void startReq(SimpleRequest req) throws IOException, ClassNotFoundException {
        StartGamePacket sgp = new StartGamePacket();
        Log.d(DEBUG_TAG, "Sending start game request...");
        objectOutputStream.writeUnshared(sgp);

        StartGamePacket response = (StartGamePacket) objectInputStream.readUnshared();
        Log.d(DEBUG_TAG,"Received: "+response.status);
        req.getCallback().onResponse(ReqType.REQ_START,response.status,0);

    }


    private void beginGame(GameCore gameCore) throws IOException {
        gameReceiveThread = new GameReceiveThread(SERVER_UPDATE_RATE, objectInputStream, gameCore);
        gameSendThread = new GameSendThread(CLIENT_INPUT_POLL_RATE, objectOutputStream, gameCore.getPlayerController());
        //begin game communication
        gameReceiveThread.start();
        gameSendThread.start();
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
}
