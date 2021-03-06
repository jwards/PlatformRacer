package coms486.jsward.platformracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import coms486.jsward.platformracer.network.NetworkManager;
import coms486.jsward.platformracer.ui.GameSelectController;
import coms486.jsward.platformracer.ui.GameSelectViewFragment;

public class GameSelectActivity extends FragmentActivity {

    private static final String DEBUG_TAG = "GameSelectActivity";

    private FrameLayout viewFrame;

    private GameSelectViewFragment gameSelectFragment;
    private GameSelectController gameSelectController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        viewFrame = findViewById(R.id.game_select_frame);
        gameSelectFragment = (GameSelectViewFragment) getSupportFragmentManager().findFragmentById(R.id.game_select_fragment);
        gameSelectController = new GameSelectController(this,gameSelectFragment);
        gameSelectFragment.setController(gameSelectController);
    }

    @Override
    public void onBackPressed() {
        if(gameSelectController !=null){
            if(gameSelectController.onBackPressed()){
                //controller handled back press
                return;
            }
        }
        //controller didn't handle the back press
        super.onBackPressed();
    }
}
