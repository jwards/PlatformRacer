package coms486.jsward.platformracer.game;

import coms486.jsward.platformracer.GameActivity;
import coms486.jsward.platformracer.User;
import jsward.platformracer.common.util.TickerThread;
import jsward.platformracer.common.game.GameCore;

public class GameThread extends TickerThread {

    private GameCore gameCore;
    private GameActivity callback;


    public GameThread(int maxTPS, GameCore gameCore, GameActivity callback) {
        super(maxTPS,false,null);
        this.gameCore = gameCore;
        this.callback = callback;
    }

    protected void tick(){
        gameCore.tick();
        if(callback.isSinglePlayer() && gameCore.getPlayerController(User.USER_ID).atEndOfLevel()){
            callback.onGameEnd(gameCore.getTime());
        }
    }

}
