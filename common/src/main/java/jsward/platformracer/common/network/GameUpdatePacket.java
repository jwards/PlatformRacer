package jsward.platformracer.common.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import jsward.platformracer.common.game.Player;

public class GameUpdatePacket implements Serializable {

    public ArrayList<Player> players;

    public GameUpdatePacket() {
    }

    public String toString(){
        if (players != null) {
            return players.toString();
        }
        return"[EMPTY]";
    }
}
