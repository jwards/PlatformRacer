package jsward.platformracer.common.network;

import java.io.Serializable;

public class JoinGamePacket implements Serializable {

    public int gameSessionId;

    public JoinGamePacket(int gameSessionId){
        this.gameSessionId = gameSessionId;
    }
}
