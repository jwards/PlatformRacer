package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import jsward.platformracer.common.game.GameCore;

import static jsward.platformracer.common.util.Constants.CLIENT_INPUT_POLL_RATE;
import static jsward.platformracer.common.util.Constants.SERVER_PORT;
import static jsward.platformracer.common.util.Constants.SERVER_UPDATE_RATE;

public class NetworkThread extends Thread {

    private static final String DEBUG_TAG = "NETWORK_THREAD";

    private static final String SERVER_ADDR  = "desktop-93rq231.student.iastate.edu";

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private RecieveSocket recieveSocket;
    private SendSocket sendSocket;

    private GameCore gameCore;


    public NetworkThread(GameCore gameCore) {
        super();
        this.gameCore = gameCore;
    }

    @Override
    public void run() {
        initNetwork();
        recieveSocket.start();
        sendSocket.start();
    }

    private void initNetwork(){
        try{
            Socket sock = new Socket(SERVER_ADDR, SERVER_PORT);
            objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
            objectInputStream = new ObjectInputStream(sock.getInputStream());

            recieveSocket = new RecieveSocket(SERVER_UPDATE_RATE, objectInputStream, gameCore);
            sendSocket = new SendSocket(CLIENT_INPUT_POLL_RATE, objectOutputStream, gameCore.getPlayerController());
        } catch (IOException e){
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }
    }
}
