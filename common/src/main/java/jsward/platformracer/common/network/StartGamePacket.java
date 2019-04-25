package jsward.platformracer.common.network;

import java.io.Serializable;

public class StartGamePacket implements Serializable {

    public Status status;

    public StartGamePacket(){
        status = Status.BAD;
    }

    public StartGamePacket(Status status){
        this.status = status;
    }

    
}
