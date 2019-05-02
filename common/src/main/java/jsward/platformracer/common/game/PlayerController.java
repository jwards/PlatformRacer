package jsward.platformracer.common.game;


import jsward.platformracer.common.util.ILogger;

public class PlayerController {

    private static final String DEBUG_TAG = "PLAYER_CONTROLLER";


    public static final long BUTTON_NULL = 0x0;
    public static final long BUTTON_LEFT = 0x1;
    public static final long BUTTON_RIGHT = 0x2;
    public static final long BUTTON_JUMP = 0x4;

    private final float JUMP_SPEED = -10f;
    private final float GRAVITY = 2f;

    private PlatformLevel level;
    private Player p;

    private float checkpoint;

    public PlayerController(PlatformLevel level,Player p){
        this.level = level;
        this.p = p;
        checkpoint = p.getX();
    }


    public void tick(){

        //every 150 units is a checkpoint
        if(((int)p.getX())%150<30){
            if (!p.isFalling() && p.canJump()) {
                //set the checkpoint when x is on the ground
                checkpoint = p.getX();
            }
        }


        //gravity
        p.setVy(p.getVy()+GRAVITY);


        if(p.getY()>300){
            //when x has fallen, reset at checkpoint
            p.setPosition(checkpoint, 25);
            p.setCanJump(true);
        }

        if ((p.controls & BUTTON_JUMP) != 0) {
            jump();
        }

        if(atEndOfLevel()) {
            //float off when you are done with the level
            p.setVy(p.getVy()-GRAVITY);
            p.setVx(0);

        } else {
            //normal movement
            if ((p.controls & BUTTON_LEFT) != 0) {
                move(-1);
            } else if ((p.controls & BUTTON_RIGHT) != 0) {
                move(1);
            } else {
                //decelerate if not moving
                p.setVx(decay(p.getVx()));
            }
        }
        //tick position
        level.detectCollision(p);

    }

    public float getX(){
        return p.getX();
    }

    public float getY(){
        return p.getY();
    }

    public void updatePosition(float x,float y,float timestep){
        float dx = Math.abs(x - getX());
        float dy = Math.abs(y - getY());
        float newX = getX();
        float newY = getY();
        if (dx <= Math.abs(p.getVx()) + Math.abs(p.getAccel())) {
            newX = x;
        } else {
            System.out.println("Bad X");
        }
        if(dy<=Math.abs(p.getVy()) + Math.abs(JUMP_SPEED)){
            newY = y;
        } else {
            System.out.println("Bad Y");
        }
        p.setPosition(x,y);
    }

    public boolean atEndOfLevel(){
        return p.getX() >= level.getEnd();
    }

    public long getControlsActive(){
        return p.controls;
    }

    public void setControlsActive(long controlsActive){
        p.controls= controlsActive;
    }

    public String getId(){
        return p.getId();
    }

    private void jump(){
        if (p.canJump()) {
            p.setVy(JUMP_SPEED);

            //canJump is reset back to true when a collision with ground is detected
            p.setCanJump(false);
        }
    }

    //1 for positive x, -1 for negative x
    private void move(int direction){
        p.setVx(p.getAccel()*direction+p.getVx());
    }

    //called by the ui thread to set which buttons are pressed
    //apply masks to control to see which buttons are active
    public void activate(long control){
        p.controls = p.controls| control;
    }

    public void deactivate(long control){
        p.controls= p.controls& ~control;
    }


    private float decay(float val){
        if (Math.abs(val) < 0.5) {
            return 0;
        } else {
            return val * 0.8f;
        }
    }

}
