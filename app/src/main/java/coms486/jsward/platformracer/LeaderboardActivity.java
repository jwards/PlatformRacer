package coms486.jsward.platformracer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import coms486.jsward.platformracer.network.NetworkManager;
import coms486.jsward.platformracer.ui.LeaderboardCallback;
import jsward.platformracer.common.game.HighScore;

public class LeaderboardActivity extends AppCompatActivity {

    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaderboard);

        ListView listview = findViewById(R.id.scoreboard_listview);
        leaderboardAdapter = new LeaderboardAdapter();
        listview.setAdapter(leaderboardAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        NetworkManager.getInstance().reqLeaderBoard(leaderboardAdapter);
    }

    private class LeaderboardAdapter extends BaseAdapter implements LeaderboardCallback {

        private ArrayList<HighScore> scores;

        public LeaderboardAdapter(){
            scores = new ArrayList<>();
        }


        @Override
        public int getCount() {
            return scores.size();
        }

        @Override
        public Object getItem(int position) {
            return scores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout;
            TextView name;
            TextView score;

            if(convertView == null){
                layout = (LinearLayout) LeaderboardActivity.this.getLayoutInflater().inflate(R.layout.scoreboard_entry, null);
            } else {
                layout = (LinearLayout) convertView;
            }
            name = layout.findViewById(R.id.scoreboard_name);
            score = layout.findViewById(R.id.scoreboard_score);

            HighScore scoredata = (HighScore) getItem(position);
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss:SS");
            Date scoretime = new Date(scoredata.getScore());

            //display name on the left and score on the right
            name.setText(scoredata.getPlayer().getName());
            score.setText(dateFormat.format(scoretime));
            
            return layout;
        }

        @Override
        public void onLeaderboardUpdate(ArrayList<HighScore> scores) {
            this.scores = scores;
            LeaderboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

}
