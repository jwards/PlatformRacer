package coms486.jsward.platformracer.game;

import android.util.Log;

public class PlayerController {

    private static final String DEBUG_TAG = "PLAYER_CONTROLLER";

    public static final long BUTTON_LEFT = 0x1;
    public static final long BUTTON_RIGHT = 0x2;
    public static final long BUTTON_JUMP = 0x4;

    private PlatformLevel level;
    private Player p;
    private long controlsActive = 0;

    private float maxvx;

    public PlayerController(PlatformLevel level,Player p){
        this.level = level;
        this.p = p;
    }

    public void update(){

        if ((controlsActive & BUTTON_JUMP) != 0) {
            movey(p.getSpeed()*3);
            Log.d(DEBUG_TAG, "pressed: jump");
        }
        if ((controlsActive & BUTTON_LEFT) != 0) {
            movex(p.getSpeed());
            Log.d(DEBUG_TAG, "pressed: left");
        }
        if ((controlsActive & BUTTON_RIGHT) != 0) {
            movex(-p.getSpeed());
            Log.d(DEBUG_TAG, "pressed: right");
        }



        //calculate the actual movement with collision detection
        float x = p.getX();
        float y = p.getY();
        x += p.getVx();
        p.setPosition(x,y);
    }

    public void activate(long control){
        controlsActive = controlsActive | control;
    }

    public void deactivate(long control){
        controlsActive = controlsActive & ~control;
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
