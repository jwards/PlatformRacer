package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.network.CreateGamePacket;
import jsward.platformracer.common.network.JoinGamePacket;
import jsward.platformracer.common.network.LobbyPacket;
import jsward.platformracer.common.network.Status;

import static jsward.platformracer.common.util.Constants.CLIENT_INPUT_POLL_RATE;
import static jsward.platformracer.common.util.Constants.SERVER_PORT;
import static jsward.platformracer.common.util.Constants.SERVER_UPDATE_RATE;

public class NetworkThread extends Thread {

    private static final String DEBUG_TAG = "NETWORK_THREAD";

    //desktop
    //private static final String SERVER_ADDR  = "desktop-93rq231.student.iastate.edu";
    //laptop
    private static final String SERVER_ADDR  = "desktop-rqgu2tp.student.iastate.edu";

    private NetworkManager callback;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private RecieveSocket recieveSocket;
    private SendSocket sendSocket;

    private boolean connectionAlive;

    private NetRequest pendingRequest;

    public NetworkThread(NetworkManager callback) {
        super();
        this.callback = callback;
        connectionAlive = false;
        pendingRequest = null;
    }

    @Override
    public synchronized void run() {
        Log.d(DEBUG_TAG,"Network thread runnning...");
        initNetwork();
        //lobby communication

        while(connectionAlive){
            try {
                Log.d(DEBUG_TAG, "Waiting for request...");
                wait();
                if(pendingRequest != null){
                    Log.d(DEBUG_TAG, "Requesting: " + pendingRequest.toString());
                    switch (pendingRequest.getType()){
                        case REQ_JOIN:
                            joinReq();
                            break;
                        case REQ_CREATE:
                            createReq();
                            break;
                        case REQ_LOBBY_LIST:
                            lobbyReq();
                            break;
                        case REQ_DESTROY:
                            break;
                    }

                    pendingRequest = null;
                }

            } catch (InterruptedException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            } catch (ClassNotFoundException e) {
                Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            }
        }
    }

    //called from seperate thread
    //returns true if request can be made
    public synchronized boolean request(NetRequest req){
        if(pendingRequest == null && connectionAlive){
            pendingRequest = req;
            notify();
            return true;
        } else {
            return false;
        }
    }

    public boolean isConnected(){
        return connectionAlive;
    }


    private void lobbyReq() throws IOException, ClassNotFoundException {
        //send request for lobby list
        Log.d(DEBUG_TAG, "Sending lobby update request...");
        objectOutputStream.writeObject(new LobbyPacket(null));
        Object obj = objectInputStream.readObject();
        if(obj != null){
            if(obj instanceof LobbyPacket){
                LobbyPacket lp = (LobbyPacket) obj;
                Log.d(DEBUG_TAG,"Received: "+lp.toString());
                pendingRequest.getCallback().onLobbyUpdated(lp.getInfos());
            } else {
                Log.d(DEBUG_TAG,"Error receiving LobbyUpdatePacket. Wrong class: "+obj.toString());
            }
        } else {
            Log.d(DEBUG_TAG,"Error receiving LobbyUpdatePacket. Null class");
        }
    }

    private void joinReq() throws IOException {
        //send request to join a game
        JoinGamePacket jgp = new JoinGamePacket(pendingRequest.getJoinSessionId());
        Log.d(DEBUG_TAG,"Sending join game request: "+jgp.toString());
        objectOutputStream.writeUnshared(jgp);

        int response = objectInputStream.readInt();
        Log.d(DEBUG_TAG, "Received: " + Status.valueOf(response));
        pendingRequest.getCallback().onResponse(Status.valueOf(response));
    }

    private void createReq() throws IOException {
        //send request to create game
        CreateGamePacket cgp = new CreateGamePacket();
        Log.d(DEBUG_TAG,"Sending create game request...");
        objectOutputStream.writeObject(cgp);

        int response = objectInputStream.readInt();
        Log.d(DEBUG_TAG, "Recieved: " + Status.valueOf(response));
        pendingRequest.getCallback().onResponse(Status.valueOf(response));

    }


    private void beginGame(GameCore gameCore) throws IOException {
        recieveSocket = new RecieveSocket(SERVER_UPDATE_RATE, objectInputStream, gameCore);
        sendSocket = new SendSocket(CLIENT_INPUT_POLL_RATE, objectOutputStream, gameCore.getPlayerController());
        //begin game communication
        recieveSocket.start();
        sendSocket.start();
    }

    private void initNetwork(){
        try{
            Log.d(DEBUG_TAG, "Attempting to connect to " + SERVER_ADDR + ":" + SERVER_PORT);
            Socket sock = new Socket(SERVER_ADDR, SERVER_PORT);
            objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
            objectInputStream = new ObjectInputStream(sock.getInputStream());
            objectOutputStream.flush();
            Log.d(DEBUG_TAG,"Connection successful");
            connectionAlive = true;
        } catch (IOException e){
            Log.d(DEBUG_TAG, "Error connecting to " + SERVER_ADDR + ":" + SERVER_PORT);
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
    }

    private void closeNetwork(){
        try {
            if(objectOutputStream != null) objectOutputStream.close();
            if(objectInputStream != null) objectInputStream.close();
            if(socket != null) socket.close();
        }catch (IOException e){
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
    }
}
