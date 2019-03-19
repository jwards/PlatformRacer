package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RecieveSocket implements Runnable {

    private static final String DEBUG_TAG = "RECIEVE_SOCKET";

    public static final int SERVER_PORT = 14914;

    public RecieveSocket(){

    }

    @Override
    public void run() {

        try {
            Socket cs = new Socket("192.168.1.30",SERVER_PORT);
            ObjectInputStream input = new ObjectInputStream(cs.getInputStream());
            while(true){
                Object obj = input.readObject();
                Log.d(DEBUG_TAG, "Recieved: " + obj.toString());
            }

        } catch (IOException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        } catch (ClassNotFoundException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
        }

    }
}
