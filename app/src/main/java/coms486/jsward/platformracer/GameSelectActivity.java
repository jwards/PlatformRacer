package coms486.jsward.platformracer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.FrameLayout;

import coms486.jsward.platformracer.network.NetworkManager;

public class GameSelectActivity extends FragmentActivity {

    private static final String DEBUG_TAG = "GameSelectActivity";

    private FrameLayout viewFrame;

    private GameSelectViewFragment gameSelectFragment;

    private NetworkManager networkManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        viewFrame = findViewById(R.id.game_select_frame);
        gameSelectFragment = (GameSelectViewFragment) getSupportFragmentManager().findFragmentById(R.id.game_select_fragment);
    }



}
