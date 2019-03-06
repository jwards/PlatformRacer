package coms486.jsward.platformracer.display;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class InputController implements View.OnTouchListener {
    private static final String DEBUG_TAG = "DISPLAY_CONTROLLER";
    private GameDisplay gameDisplay;

    private float[] pointDown = new float[2];
    private float[] pointUp = new float[2];

    public static final int BUTTON_LEFT = 1;
    public static final int BUTTON_RIGHT = 2;
    public static final int BUTTON_JUMP = 3;


    public InputController(GameDisplay gameDisplay){
        this.gameDisplay = gameDisplay;
        gameDisplay.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pointDown[0] = event.getX();
                pointDown[1] = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                pointUp[0] = event.getX();
                pointUp[1] = event.getY();
                checkButtons();
                return true;

        }
        return false;
    }

    private void onButtonClicked(int tag){
        Log.d(DEBUG_TAG, "Button " + tag + " clicked.");
        switch (tag){
            case BUTTON_JUMP:

                break;
            case BUTTON_LEFT:

                break;
            case BUTTON_RIGHT:

                break;
        }
    }

    private void checkButtons(){
        for (SVButton svb : gameDisplay.getButtons()) {
            if(svb.wasClicked(pointDown, pointUp))
                onButtonClicked(svb.getTag());
        }
    }
}
