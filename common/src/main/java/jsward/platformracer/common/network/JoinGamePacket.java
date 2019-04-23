package jsward.platformracer.common.network;

import java.io.Serializable;

public class JoinGamePacket implements Serializable {

    public int gameSessionId;

    //indicates status of request to requester
    public Status status;


    //if gamesessionid = -1 then leave game
    public JoinGamePacket(int gameSessionId){
        this.gameSessionId = gameSessionId;
    }

    @Override
    public String toString() {
        return "[JoinGame, " + gameSessionId + ", " + status + "]";
    }
}
