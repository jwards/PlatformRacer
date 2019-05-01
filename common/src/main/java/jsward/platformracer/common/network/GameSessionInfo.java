package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

import jsward.platformracer.common.util.UserInfo;

public class GameSessionInfo implements Serializable {

    public final int lobbyId;
    public final int capacity;
    public final int maxCapacity;

    public UserInfo hostPlayer;

    public ArrayList<UserInfo> players;

    public GameSessionInfo(int lobbyId,UserInfo hostPlayer,int capacity,int maxCapacity,ArrayList<UserInfo> players){
        this.lobbyId = lobbyId;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.hostPlayer= hostPlayer;
        this.players = players;
    }

    public boolean ready(){
        if(players != null){
            return players.size() == maxCapacity;
        }
        return false;
    }

    public boolean isHost(String playerId){
        return playerId == hostPlayer.getId();
    }

    @Override
    public String toString() {
        return "[ID: "+lobbyId+ ", Capacity: "+capacity+"/"+maxCapacity+", HostName: "+hostPlayer.getName()+", HostId: "+ hostPlayer.getId()+ ", Players: "+players.toString()+"]";
    }
}
