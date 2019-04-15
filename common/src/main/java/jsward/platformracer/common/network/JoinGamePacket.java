package jsward.platformracer.common.network;

import java.io.Serializable;

public class JoinGamePacket implements Serializable {

    public int gameSessionId;

    //indicates status of request to requester
    public Status status;

    public JoinGamePacket(int gameSessionId){
        this.gameSessionId = gameSessionId;
    }
}
