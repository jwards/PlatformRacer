package jsward.platformracer.common.network;

import java.io.Serializable;

public class GameSessionInfo implements Serializable {

    public final int lobbyId;
    public final int capacity;
    public final int maxCapacity;

    public final String hostPlayerName;

    public GameSessionInfo(int lobbyId,String hostPlayerName,int capacity,int maxCapacity){
        this.lobbyId = lobbyId;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.hostPlayerName = hostPlayerName;
    }

    @Override
    public String toString() {
        return "[ID: "+lobbyId+ ", Capacity: "+capacity+"/"+maxCapacity+", Host: "+hostPlayerName+"]";
    }
}
