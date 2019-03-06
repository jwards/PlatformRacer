package coms486.jsward.platformracer.game;

public class Player {

    //horizontal position on the level
    private long position;

    //vertical position
    private float height;


    private long speed;

    public Player(long position,long speed){
        this.position = position;
        this.speed = speed;

    }

    public long getPosition(){
        return position;
    }

    public void move(){

    }

    public void jump(){

    }
}
