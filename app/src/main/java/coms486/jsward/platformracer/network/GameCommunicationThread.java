package coms486.jsward.platformracer.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.ObjIntConsumer;

import coms486.jsward.platformracer.AndroidLogger;
import coms486.jsward.platformracer.User;
import jsward.platformracer.common.game.Player;
import jsward.platformracer.common.game.PlayerController;
import jsward.platformracer.common.util.Constants;
import jsward.platformracer.common.util.TickerThread;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.network.GameUpdatePacket;

public class GameCommunicationThread extends TickerThread {

    private static final String DEBUG_TAG = "RECIEVE_SOCKET";

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private GameCore gameCore;
    private PlayerController controller;

    private GameUpdatePacket gameUpdatePacket;

    private int counter;

    public GameCommunicationThread(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, GameCore gameCore) throws IOException {
        super(Constants.SERVER_UPDATE_RATE,false,new AndroidLogger());
        this.gameCore = gameCore;
        input = objectInputStream;
        output = objectOutputStream;
        counter = 0;
    }

    public void getGameInfo() {
        try {
            Object obj = input.readUnshared();
            if (obj instanceof GameUpdatePacket) {
                gameUpdatePacket = (GameUpdatePacket) obj;
                gameCore.initGame(gameUpdatePacket);
                //set the game controller
                controller = gameCore.getPlayerController(User.USER_ID);
            } else {
                Log.d(DEBUG_TAG, "Error: received unexpected object type");
            }
        } catch (ClassNotFoundException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        }
    }

    private void readUpdate() {
        try {
            Object obj = input.readUnshared();
            if (obj instanceof GameUpdatePacket) {
                gameUpdatePacket = (GameUpdatePacket) obj;
                //update players
                for (int i = 0; i < gameUpdatePacket.players.size(); i++) {
                    Player updated = gameUpdatePacket.players.get(i);
                    Player local = gameCore.getPlayer(updated.getId());
                    if (updated.getId().equals(User.USER_ID)) {
                        //gameCore.updateLocalPlayer(local, updated);
                        //controller.updatePosition(updated.getX(), updated.getY(), 1f / Constants.SERVER_UPDATE_RATE);
                    } else {
                        gameCore.updatePlayer(local, updated);
                    }
                        /*
                        for(Player p:gameCore.getAllPlayers()){
                            Log.d(DEBUG_TAG,p+ " : ");
                        }*/
                }
            } else {
                Log.d(DEBUG_TAG, "Error: received unexpected object type");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void sendUpdate(long controls,float x,float y) {
        try{
            output.reset();
            output.writeLong(controls);
            output.writeFloat(x);
            output.writeFloat(y);
            output.flush();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
            hault();
        }
    }

    @Override
    protected void tick() {
        sendUpdate(controller.getControlsActive(), controller.getX(), controller.getY());
        readUpdate();
    }

}
