package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyPacket implements Serializable {


    private ArrayList<GameSessionInfo> gameSessions;

    public LobbyPacket(ArrayList<GameSessionInfo> infos){
        gameSessions = infos;
    }

    public ArrayList<GameSessionInfo> getInfos(){
        return gameSessions;
    }
}
