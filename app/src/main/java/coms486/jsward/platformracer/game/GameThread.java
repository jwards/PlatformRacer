package coms486.jsward.platformracer.game;

import android.util.Log;

public class GameThread extends Thread {
    private static final String DEBUG_TAG = "DISPLAY_THREAD";
    public static final int MAX_FPS = 60;
    private double averageFPS;
    private boolean running;



    public GameThread() {
        super();

        running = false;
    }

    @Override
    public void run() {

        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        running = true;
        while(true){
            if(running) {
                startTime = System.nanoTime();

                //TODO
                //tick

                timeMillis = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMillis;
                try {
                    if (waitTime > 0) {
                        this.sleep(waitTime);
                    }

                } catch (Exception e) {
                    Log.d(DEBUG_TAG, Log.getStackTraceString(e));
                }

                totalTime += System.nanoTime() - startTime;
                frameCount++;
                if (frameCount == MAX_FPS) {
                    averageFPS = ((float)frameCount/totalTime)*.0000000001;
                    frameCount = 0;
                    totalTime = 0;
                    Log.d(DEBUG_TAG, "FPS: " + averageFPS);
                }
            } else {
                //wait here until game is resumed

            }
        }

    }

}
