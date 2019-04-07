package jsward.platformracer.common.util;

public interface ILogger {

    void logMessage(String tag,String message);
    void logException(String tag,Exception e);
}
