package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.ObjIntConsumer;

import coms486.jsward.platformracer.AndroidLogger;
import jsward.platformracer.common.util.TickerThread;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.network.GameUpdatePacket;

public class GameReceiveThread extends TickerThread {

    private static final String DEBUG_TAG = "RECIEVE_SOCKET";

    private ObjectInputStream input;

    private GameCore gameCore;

    public GameReceiveThread(int maxTPS, ObjectInputStream objectInputStream, GameCore gameCore) throws IOException {
        super(maxTPS,false,new AndroidLogger());
        this.gameCore = gameCore;
        input = objectInputStream;

    }

    private void readUpdate(){
        GameUpdatePacket gup = null;
        try {
            Object obj = input.readUnshared();
            if (obj != null) {
                if (obj instanceof GameUpdatePacket) {
                    gup = (GameUpdatePacket) obj;
                    gameCore.update(gup);
                } else {
                    Log.d(DEBUG_TAG, "Error: received unexpected object type");
                }
            } else {
                Log.d(DEBUG_TAG, "Error: received null object");
            }
        } catch (ClassNotFoundException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        }
    }

    @Override
    protected void tick() {
        readUpdate();
    }

}
