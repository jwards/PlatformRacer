package coms486.jsward.platformracer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import coms486.jsward.platformracer.display.DrawLevel;
import coms486.jsward.platformracer.display.GameDrawer;
import coms486.jsward.platformracer.display.InputController;
import coms486.jsward.platformracer.display.DisplayThread;
import coms486.jsward.platformracer.display.GameDisplay;
import coms486.jsward.platformracer.display.SVButton;
import coms486.jsward.platformracer.game.GameThread;
import coms486.jsward.platformracer.network.NetworkManager;
import jsward.platformracer.common.game.GameCore;
import jsward.platformracer.common.game.PlatformLevel;
import jsward.platformracer.common.game.PlayerController;

import static jsward.platformracer.common.util.Constants.GAME_LOOP_MAX_TPS;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "GAME_ACTIVITY";

    public static final String FLAG_SINGLEPLAYER = "GameActivity.FLAG_SINGLEPLAYER";
    private boolean isSinglePlayer;

    private GameCore gameCore;
    private GameThread gameThread;

    private GameDisplay gameDisplay;
    private DisplayThread displayThread;
    private DrawLevel levelDrawer;
    private GameDrawer gameDrawer;
    private ArrayList<Bitmap> playerSprites;

    private static boolean displaying;

    private Point displaySize;

    //used to uiScale the ui
    private float uiScale;

    //button relative positions
    private final float B_UP_X = 0.8f;
    private final float B_UP_Y = 0.85f;

    private final float B_LEFT_X = 0.05f;
    private final float B_LEFT_Y = 0.85f;

    private final float B_RIGHT_X = 0.13f;
    private final float B_RIGHT_Y = 0.85f;

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

        //calcuate uiScale
        displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        if(displaySize.x <1400){
            uiScale = .9f;
        } else if(displaySize.x <2000){
            uiScale = 1.2f;
        } else {
            uiScale = 1.5f;
        }

        readIntent(getIntent());

        Log.d(DEBUG_TAG, "Initializing game...");
        initGame();

        Log.d(DEBUG_TAG, "Initializing buttons...");
        initDisplayButtons();
        Log.d(DEBUG_TAG, "Initializing display...");
        initDisplay();

        if(!isSinglePlayer){
            NetworkManager.getInstance().beginGame(gameCore,this);
        }

        if(isSinglePlayer){
            //if this is singleplayer this won't be called by the network thread so we call it here instead
            gameCore.addPlayer(User.USER_ID);
            gameCore.addPlayerController(User.USER_ID);
            onGameCoreInit();

        }


        displaying = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        displayThread.stopDisplay();
        gameThread.stopRunning();
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



        gameDisplay.addButton(new SVButton(uiScale,displaySize.x*B_LEFT_X,displaySize.y*B_LEFT_Y, PlayerController.BUTTON_LEFT,btnLeft));
        gameDisplay.addButton(new SVButton(uiScale,displaySize.x*B_RIGHT_X,displaySize.y*B_RIGHT_Y,PlayerController.BUTTON_RIGHT,btnRight));
        gameDisplay.addButton(new SVButton(uiScale,displaySize.x*B_UP_X,displaySize.y*B_UP_Y,PlayerController.BUTTON_JUMP,btnUp));
    }

    private void initDisplay() {
        //load player sprites
        Bitmap playerSprite = BitmapUtil.getBitmapFromAsset(this, "player_sprite.png");

        playerSprites = new ArrayList<>();

        for(int i = 0;i<6;i++){
            playerSprites.add(Bitmap.createBitmap(playerSprite, 100 * i, 0, 100, 150));
        }

        gameDrawer = new GameDrawer(gameDisplay);
        displayThread = new DisplayThread(gameDisplay.getHolder(), gameDrawer);
    }

    private void setController(){
        InputController controller = new InputController(gameDisplay, gameCore.getPlayerController(User.USER_ID));
        gameDisplay.setOnTouchListener(controller);
    }

    //called when the game core has been updated with the player info from the server
    public void onGameCoreInit(){
        levelDrawer = new DrawLevel(playerSprites,displaySize);
        gameDrawer.addDrawable(levelDrawer);
        levelDrawer.setPlayer(gameCore.getPlayer(User.USER_ID));
        levelDrawer.setPlayerList(gameCore.getAllPlayers());
        setController();
        if(!displaying) {
            displayThread.start();
            gameThread.start();
            displaying = true;
        }
    }


}
