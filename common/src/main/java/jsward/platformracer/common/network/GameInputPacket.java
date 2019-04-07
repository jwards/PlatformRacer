package jsward.platformracer.common.network;

import java.io.Serializable;

public class GameInputPacket implements Serializable {

    private long controls;

    public GameInputPacket(long controls){
        this.controls = controls;
    }

    public GameInputPacket(){
        controls = 0;
    }


    public long getControls() {
        return controls;
    }

    public void setControls(long controls) {
        this.controls = controls;
    }

    public String toString(){
        return "Controls: " + Long.toHexString(controls);
    }
}
