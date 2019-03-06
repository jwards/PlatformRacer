package coms486.jsward.platformracer.game;

public class Player {

    private float x;
    private float y;
    private float vx;
    private float vy;

    private float speed;

    public Player(float x,float y,float speed){
        this.speed = speed;
        this.x=x;
        this.y=y;

    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public float getVx(){
        return vx;
    }

    public float getVy(){
        return vy;
    }

    public float getSpeed(){
        return speed;
    }

    public void setPosition(float x,float y){
        this.x=x;
        this.y=y;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public void setVy(float vy){
        this.vy = vy;
    }
}
