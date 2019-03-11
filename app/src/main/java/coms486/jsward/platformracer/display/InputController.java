package coms486.jsward.platformracer.display;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Hashtable;
import java.util.LinkedList;

import coms486.jsward.platformracer.game.PlayerController;

import static coms486.jsward.platformracer.game.PlayerController.BUTTON_NULL;


public class InputController implements View.OnTouchListener {
    private static final String DEBUG_TAG = "DISPLAY_CONTROLLER";

    private GameDisplay gameDisplay;
    private PlayerController playerController;

    private float[] pointDown = new float[2];


    public InputController(GameDisplay gameDisplay, PlayerController playerController){
        this.gameDisplay = gameDisplay;
        this.playerController = playerController;
        gameDisplay.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int maskedAction = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerCount = event.getPointerCount();

        //bug: when secondary pointer is released, any button it's pressing will not be deactivated
        //until the next MotionEvent.
        if (maskedAction == MotionEvent.ACTION_MOVE) {
            //reset all buttons
            playerController.deactivate(~0);
            for (int i = 0; i < pointerCount; i++) {
                pointDown[0] = event.getX(i);
                pointDown[1] = event.getY(i);
                long button = getButton(pointDown);
                playerController.activate(button);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            //subscribe to event
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //upress all buttons
            playerController.deactivate(~0);
            return true;
        }

        return false;
    }


    //returns the tag of the button that the point was in
    // returns 0 if point wasn't in any button
    private long getButton(float[] point){
        for (SVButton svb : gameDisplay.getButtons()) {
            if(svb.inButton(point)) {
                return svb.getTag();
            }
        }
        return 0;
    }
}
