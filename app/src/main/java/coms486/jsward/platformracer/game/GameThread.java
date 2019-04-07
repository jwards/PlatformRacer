package coms486.jsward.platformracer.game;

import jsward.platformracer.common.util.TickerThread;
import jsward.platformracer.common.game.GameCore;

public class GameThread extends TickerThread {

    private GameCore gameCore;

    public GameThread(int maxTPS,GameCore gameCore) {
        super(maxTPS,false,null);
        this.gameCore = gameCore;
    }

    protected void tick(){
        gameCore.tick();
    }

}
