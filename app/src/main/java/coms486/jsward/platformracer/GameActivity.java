package coms486.jsward.platformracer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import coms486.jsward.platformracer.display.InputController;
import coms486.jsward.platformracer.display.DisplayThread;
import coms486.jsward.platformracer.display.GameDisplay;
import coms486.jsward.platformracer.display.SVButton;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "GAME_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //initialize the display
        GameDisplay gameDisplay = findViewById(R.id.gameDisplay);

        if(gameDisplay==null){
            Log.e(DEBUG_TAG, "Could not get GameDisplay from view!\nExiting GameActivity");
            finish();
        }

        InputController inputController = new InputController(gameDisplay);
        DisplayThread displayThread;
        displayThread = new DisplayThread(gameDisplay.getHolder(), gameDisplay);

        //load button sprites
        Bitmap buttonSprites = BitmapUtil.getBitmapFromAsset(this, "dir_buttons.png");

        Bitmap btnLeft = Bitmap.createBitmap(buttonSprites, 0, 0, 100, 100);
        Bitmap btnRight = Bitmap.createBitmap(buttonSprites, 100, 0, 100, 100);
        Bitmap btnUp = Bitmap.createBitmap(buttonSprites, 200, 0, 100, 100);

        //TODO find comfortable place for buttons to be
        gameDisplay.addButton(new SVButton(100,100,500,150,InputController.BUTTON_LEFT,btnLeft));
        gameDisplay.addButton(new SVButton(100,100,300,200,InputController.BUTTON_RIGHT,btnRight));
        gameDisplay.addButton(new SVButton(100,100,600,500,InputController.BUTTON_JUMP,btnUp));


        displayThread.start();
    }



}
