package coms486.jsward.platformracer.game;

public class PlayerController {

    private PlatformLevel level;
    private Player p;

    private float maxvx;

    public PlayerController(PlatformLevel level,Player p){
        this.level = level;
        this.p = p;
    }

    public void move(){
        //calculate the actual movement with collision detection
        float x = p.getX();
        float y = p.getY();
        x += p.getVx();
        p.setPosition(x,y);
    }

    public void moveLeft(){
        movex(-p.getSpeed());
    }

    public void moveRight(){
        movex(p.getSpeed());
    }

    public void jump(){
        movey(p.getSpeed()*3);
    }

    private void movex(float dvx){
        float vx;
        vx = bound(p.getVx() + dvx, -3, 3);
        p.setVx(vx);
    }

    private void movey(float dvy){
        float vy;
        vy = bound(p.getVy() + dvy, -5, 5);
        p.setVy(vy);
    }

    private float bound(float test,float lb,float ub){
        if(test<lb){
            return lb;
        }
        if(test>ub){
            return ub;
        }
        return test;
    }

}
