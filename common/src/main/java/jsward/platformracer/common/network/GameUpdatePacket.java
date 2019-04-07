package jsward.platformracer.common.network;

import java.io.Serializable;

import jsward.platformracer.common.game.Player;

public class GameUpdatePacket implements Serializable {

    private float px,py,pvx,pvy,ox,oy,ovx,ovy;

    public GameUpdatePacket() {
    }

    public GameUpdatePacket setPlayer(Player p){
        px = p.getX();
        py = p.getY();
        pvx = p.getVx();
        pvy = p.getVy();
        return this;
    }

    public GameUpdatePacket setOpponent(Player p){
        ox = p.getX();
        oy = p.getY();
        ovx = p.getVx();
        ovy = p.getVy();
        return this;
    }

    public void updatePlayer(Player p){
        p.setPosition(px, py);
        p.setVx(pvx);
        p.setVy(pvy);
    }

    public void updateOpponent(Player p){
        p.setPosition(ox,oy);
        p.setVx(ovx);
        p.setVy(ovy);
    }

    public float getPx() {
        return px;
    }

    public float getPy() {
        return py;
    }

    public float getPvx() {
        return pvx;
    }

    public float getPvy() {
        return pvy;
    }

    public float getOx() {
        return ox;
    }

    public float getOy() {
        return oy;
    }

    public float getOvx() {
        return ovx;
    }

    public float getOvy() {
        return ovy;
    }

    public String toString(){
        return "{P1["+px+", "+py+", "+pvx+", "+pvy+"],["+ox+", "+oy+", "+ovx+", "+ovy+"] }";
    }
}
