package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSessionInfo implements Serializable {

    public final int lobbyId;
    public final int capacity;
    public final int maxCapacity;

    public final int hostPlayerId;

    public ArrayList<String> players;

    public GameSessionInfo(int lobbyId,int hostPlayerId,int capacity,int maxCapacity,ArrayList<String> players){
        this.lobbyId = lobbyId;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.hostPlayerId = hostPlayerId;
        this.players = players;
    }

    @Override
    public String toString() {
        return "[ID: "+lobbyId+ ", Capacity: "+capacity+"/"+maxCapacity+", HostId: "+hostPlayerId+ ", Players: "+players.toString()+"]";
    }
}
