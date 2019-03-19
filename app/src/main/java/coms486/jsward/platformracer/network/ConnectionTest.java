package coms486.jsward.platformracer.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import coms486.jsward.platformracer.R;

public class ConnectionTest extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_test);

        Log.d("CONNECTION_TEST", "opening connection...");
        RecieveSocket rs = new RecieveSocket();
        Thread socketThread = new Thread(rs);
        socketThread.start();
    }
}
