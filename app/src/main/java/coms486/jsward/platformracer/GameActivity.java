package coms486.jsward.platformracer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import coms486.jsward.platformracer.display.DrawLevel;
import coms486.jsward.platformracer.display.GameDrawer;
import coms486.jsward.platformracer.display.InputController;
import coms486.jsward.platformracer.display.DisplayThread;
import coms486.jsward.platformracer.display.GameDisplay;
import coms486.jsward.platformracer.display.SVButton;
import coms486.jsward.platformracer.game.GameThread;
import coms486.jsward.platformracer.network.NetworkThread;
import coms486.jsward.platformracer.network.RecieveSocket;
import coms486.jsward.platformracer.network.SendSocket;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.game.PlatformLevel;
import jsward.platformracer.common.game.PlayerController;

import static jsward.platformracer.common.util.Constants.CLIENT_INPUT_POLL_RATE;
import static jsward.platformracer.common.util.Constants.GAME_LOOP_MAX_TPS;
import static jsward.platformracer.common.util.Constants.SERVER_PORT;
import static jsward.platformracer.common.util.Constants.SERVER_UPDATE_RATE;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "GAME_ACTIVITY";

    public static final String FLAG_SINGLEPLAYER = "GameActivity.FLAG_SINGLEPLAYER";
    private boolean isSinglePlayer;

    private GameCore gameCore;
    private GameThread gameThread;

    private GameDisplay gameDisplay;
    private DisplayThread displayThread;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        //initialize the display
        gameDisplay = findViewById(R.id.gameDisplay);

        if(gameDisplay==null){
            Log.e(DEBUG_TAG, "Could not get GameDisplay from view!\nExiting GameActivity");
            finish();
        }

        readIntent(getIntent());

        if(!isSinglePlayer){
            //network stuff
        }

        initGame();
        initDisplayButtons();
        initDisplay();
        setController();



        displayThread.start();
        gameThread.start();
    }

    private void initGame(){
        PlatformLevel platformLevel = new PlatformLevel();
        gameCore = new GameCore(platformLevel);
        gameThread = new GameThread(GAME_LOOP_MAX_TPS,gameCore);
    }

    private void readIntent(Intent intent){
        isSinglePlayer = intent.getBooleanExtra(FLAG_SINGLEPLAYER,true);
    }


    private void initDisplayButtons(){
        //load button sprites
        Bitmap buttonSprites = BitmapUtil.getBitmapFromAsset(this, "dir_buttons.png");

        Bitmap btnRight = Bitmap.createBitmap(buttonSprites, 0, 0, 99, 99);
        Bitmap btnLeft = Bitmap.createBitmap(buttonSprites, 99, 0, 99, 99);
        Bitmap btnUp = Bitmap.createBitmap(buttonSprites, 198, 0, 99, 99);

        //TODO make button location relative to screen size
        gameDisplay.addButton(new SVButton(150,150,100,950, PlayerController.BUTTON_LEFT,btnLeft));
        gameDisplay.addButton(new SVButton(150,150,300,950,PlayerController.BUTTON_RIGHT,btnRight));
        gameDisplay.addButton(new SVButton(150,150,1800,950,PlayerController.BUTTON_JUMP,btnUp));
    }

    private void initDisplay() {
        //load player sprites
        Bitmap playerSprite = BitmapUtil.getBitmapFromAsset(this, "player_sprite.png");

        ArrayList<Bitmap> psprites = new ArrayList<>();

        for(int i = 0;i<6;i++){
            psprites.add(Bitmap.createBitmap(playerSprite, 100 * i, 0, 100, 150));
        }

        GameDrawer gameDrawer = new GameDrawer(gameDisplay);
        DrawLevel levelDrawer = new DrawLevel(gameCore.getPlayer(), psprites);
        gameDrawer.addDrawable(levelDrawer);

        displayThread = new DisplayThread(gameDisplay.getHolder(), gameDrawer);
    }

    private void setController(){
        InputController controller = new InputController(gameDisplay, gameCore.getPlayerController());
        gameDisplay.setOnTouchListener(controller);
    }


}
