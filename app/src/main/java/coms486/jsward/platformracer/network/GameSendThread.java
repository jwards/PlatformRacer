package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import coms486.jsward.platformracer.AndroidLogger;
import jsward.platformracer.common.util.TickerThread;
import jsward.platformracer.common.game.PlayerController;
import jsward.platformracer.common.network.GameInputPacket;

public class GameSendThread extends TickerThread {


    private static final String DEBUG_TAG ="SEND_SOCKET";
    private ObjectOutputStream out;
    private GameInputPacket gameInputPacket;
    private PlayerController controller;

    public GameSendThread(int maxTPS, ObjectOutputStream objectOutputStream , PlayerController controller) throws IOException {
        super(maxTPS,true,new AndroidLogger());
        out = objectOutputStream;
        gameInputPacket = new GameInputPacket();
        this.controller = controller;
    }


    private void sendUpdate(GameInputPacket gameInputPacket) {
        try{
            out.writeUnshared(gameInputPacket);
        } catch (IOException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        }
    }

    @Override
    protected void tick() {
        gameInputPacket.setControls(controller.getControlsActive());
        sendUpdate(gameInputPacket);
    }
}
