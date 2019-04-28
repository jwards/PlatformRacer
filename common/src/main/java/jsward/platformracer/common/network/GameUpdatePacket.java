package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;

import jsward.platformracer.common.game.Player;

public class GameUpdatePacket implements Serializable {

    public Player client;
    public ArrayList<Player> otherPlayers;

    public GameUpdatePacket() {
        otherPlayers = new ArrayList<>();
    }

    public String toString(){
        String str = "{";
        if(client!=null){
            str += client.toString();
        }
        if (otherPlayers != null) {
            str += ", ";
            str += otherPlayers.toString();
        }
        str += "}";
        return str;
    }
}
