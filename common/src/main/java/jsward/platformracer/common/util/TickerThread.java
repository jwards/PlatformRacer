package jsward.platformracer.common.util;

import jsward.platformracer.common.util.ILogger;

public abstract class TickerThread extends Thread {
    protected final String DEBUG_TAG = "TICKER_THREAD";


    private final boolean LOG_TPS;
    private final int MAX_TPS;

    private ILogger logger;

    private double averageTPS;
    private boolean running;


    public TickerThread(int maxTPS, boolean enableLogging,ILogger logger) {
        super();
        this.MAX_TPS =  maxTPS;
        LOG_TPS = enableLogging;
        this.logger = logger;
        running = false;
    }

    protected abstract void tick();

    public void hault() {
        running = false;
    }

    @Override
    public void run() {

        long startTime;
        long timeMillis = 1000 / MAX_TPS;
        long waitTime;
        int tickCount = 0;
        long totalTime = 0;
        long targetTime = 1000 / MAX_TPS;

        running = true;
        while (running) {
            startTime = System.nanoTime();

            tick();

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }

            } catch (Exception e) {
                logger.logException(DEBUG_TAG, e);
            }

            totalTime += System.nanoTime() - startTime;
            tickCount++;
            if (tickCount == MAX_TPS) {
                averageTPS = ((float) tickCount / totalTime) * 1000000000;
                tickCount = 0;
                totalTime = 0;
                if (LOG_TPS) logger.logMessage(DEBUG_TAG, "TPS: " + averageTPS);
            }
        }

    }

}
