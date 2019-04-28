package coms486.jsward.platformracer.display;

import android.view.MotionEvent;
import android.view.View;

import jsward.platformracer.common.game.PlayerController;

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

        if (maskedAction == MotionEvent.ACTION_MOVE) {
            updateButtons(pointerCount,event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            //subscribe to event
            updateButtons(pointerCount,event);
            return true;
        }
        if(maskedAction == MotionEvent.ACTION_UP || maskedAction == MotionEvent.ACTION_POINTER_UP){
            //find which button was pressed and deactivate it
            pointDown[0] = event.getX(event.getActionIndex());
            pointDown[1] = event.getY(event.getActionIndex());
            playerController.deactivate(getButton(pointDown));
        }

        return false;
    }

    private void updateButtons(int pointerCount,MotionEvent event){
        //reset all buttons
        playerController.deactivate(~0);
        for (int i = 0; i < pointerCount; i++) {
            pointDown[0] = event.getX(i);
            pointDown[1] = event.getY(i);
            long button = getButton(pointDown);
            playerController.activate(button);
        }
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
