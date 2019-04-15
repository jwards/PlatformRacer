package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyPacket implements Serializable {

    private ArrayList<GameSessionInfo> gameSessions;
    private int lobbyId;

    public LobbyPacket(ArrayList<GameSessionInfo> infos,int lobbyId){
        gameSessions = infos;
        this.lobbyId = lobbyId;
    }

    public ArrayList<GameSessionInfo> getInfos(){
        return gameSessions;
    }

    public int getLobbyId(){
        return lobbyId;
    }
}
