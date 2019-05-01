package coms486.jsward.platformracer.ui;

import java.util.ArrayList;

import coms486.jsward.platformracer.network.NCallback;
import jsward.platformracer.common.game.HighScore;

public interface LeaderboardCallback extends NCallback {
    void onLeaderboardUpdate(ArrayList<HighScore> scores);
}
