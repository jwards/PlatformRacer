package jsward.platformracer.common.game;

import java.io.Serializable;

import jsward.platformracer.common.util.UserInfo;

public class HighScore implements Serializable {

    private UserInfo player;
    private long score;

    public HighScore(UserInfo player,long score){
        this.player = player;
        this.score =score;
    }


    public UserInfo getPlayer() {
        return player;
    }

    public void setPlayer(UserInfo player) {
        this.player = player;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

