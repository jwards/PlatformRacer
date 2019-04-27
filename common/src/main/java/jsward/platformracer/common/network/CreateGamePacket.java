package jsward.platformracer.common.network;

import java.io.Serializable;

public class CreateGamePacket implements Serializable {

    public int gameSessionId;
    public Status status;

    public CreateGamePacket(int gameSessionId,Status status){
        this.gameSessionId = gameSessionId;
        this.status = status;
    }

    public CreateGamePacket(){
        this(0, Status.BAD);
    }


}
