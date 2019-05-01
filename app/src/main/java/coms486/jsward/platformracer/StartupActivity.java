package coms486.jsward.platformracer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import coms486.jsward.platformracer.network.NetworkManager;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Button startButton = findViewById(R.id.startup_play);
        startButton.setTag(1);

        Button leaderButton = findViewById(R.id.startup_leaderboard);
        leaderButton.setTag(2);

        ButtonListener buttonListener = new ButtonListener();
        startButton.setOnClickListener(buttonListener);
        leaderButton.setOnClickListener(buttonListener);

        //connect to network
        NetworkManager networkManager = NetworkManager.getInstance();
    }

    private void launch(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch ((int)v.getTag()){
                case 1:
                    //start game
                    launch(GameSelectActivity.class);
                    break;
                case 2:
                    //open leaderboards
                    launch(LeaderboardActivity.class);
                    break;
            }
        }
    }
}
