package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

import jsward.platformracer.common.game.HighScore;
import jsward.platformracer.common.util.UserInfo;

public class LeaderBoardPacket implements Serializable {

    public ArrayList<HighScore> scores;


    public LeaderBoardPacket(){

    }
}
