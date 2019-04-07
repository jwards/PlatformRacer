package coms486.jsward.platformracer;

import android.util.Log;

import jsward.platformracer.common.util.ILogger;

public class AndroidLogger implements ILogger {


    @Override
    public void logMessage(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void logException(String tag, Exception e) {
        Log.d(tag, Log.getStackTraceString(e));
    }
}
