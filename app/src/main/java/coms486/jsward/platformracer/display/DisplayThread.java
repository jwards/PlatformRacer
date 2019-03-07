package coms486.jsward.platformracer.display;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DisplayThread extends Thread {
    private static final String DEBUG_TAG = "DISPLAY_THREAD";
    private static final boolean LOG_FPS = false;


    public static final int MAX_FPS = 30;
    private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private GameDrawer gameDrawer;
    private boolean running;
    private Object lock = new Object();

    private static Canvas canvas;

    public DisplayThread(SurfaceHolder sh,GameDrawer gameDrawer) {
        super();
        this.surfaceHolder = sh;
        this.gameDrawer = gameDrawer;
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
                canvas = null;

                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        //don't try to draw if the view isn't on screen
                        if (canvas != null) {
                            gameDrawer.draw(canvas);

                        }

                    }

                } catch (Exception e) {
                    //can't draw to surface so skip this frame
                    Log.d(DEBUG_TAG, Log.getStackTraceString(e));
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            Log.d(DEBUG_TAG, Log.getStackTraceString(e));
                        }

                    }
                }
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
                    averageFPS = ((float)frameCount/(float)totalTime)*1000000000;
                    frameCount = 0;
                    totalTime = 0;
                    if (LOG_FPS) Log.d(DEBUG_TAG, "FPS: " + averageFPS);
                }
            } else {
                //wait here until game is resumed
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Log.d(DEBUG_TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }

    }

}
