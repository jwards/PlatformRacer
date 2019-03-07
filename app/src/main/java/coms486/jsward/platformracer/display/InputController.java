package coms486.jsward.platformracer.display;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import coms486.jsward.platformracer.game.PlayerController;

import static coms486.jsward.platformracer.game.PlayerController.BUTTON_JUMP;
import static coms486.jsward.platformracer.game.PlayerController.BUTTON_LEFT;
import static coms486.jsward.platformracer.game.PlayerController.BUTTON_RIGHT;


public class InputController implements View.OnTouchListener {
    private static final String DEBUG_TAG = "DISPLAY_CONTROLLER";

    private GameDisplay gameDisplay;
    private PlayerController playerController;

    private float[] pointDown = new float[2];
    private float[] pointUp = new float[2];




    public InputController(GameDisplay gameDisplay, PlayerController playerController){
        this.gameDisplay = gameDisplay;
        this.playerController = playerController;
        gameDisplay.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pointDown[0] = event.getX();
        pointDown[1] = event.getY();
        checkButtons();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            playerController.deactivate(~0);
        }
        return true;
    }


    private void checkButtons(){
        for (SVButton svb : gameDisplay.getButtons()) {
            if(svb.inButton(pointDown)) {
                playerController.activate(svb.getTag());
            } else {
                playerController.deactivate(svb.getTag());
            }
        }
    }
}
