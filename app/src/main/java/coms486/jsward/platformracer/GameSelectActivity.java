package coms486.jsward.platformracer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class GameSelectActivity extends AppCompatActivity {

    private FrameLayout viewFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        viewFrame = findViewById(R.id.game_select_frame);
    }



}
