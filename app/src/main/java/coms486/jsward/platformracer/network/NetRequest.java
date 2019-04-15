package coms486.jsward.platformracer.network;

import android.support.annotation.NonNull;

import java.util.UUID;

import jsward.platformracer.common.network.ReqType;

public abstract class NetRequest implements Comparable<NetRequest> {

    protected ReqType type;

    private final String REQUEST_ID = UUID.randomUUID().toString();
    private final long interval;
    private final int count;
    protected int runCounter;
    protected long lastRun;

    public NetRequest(ReqType type,long interval,int count){
        this.type = type;
        this.interval = interval;
        this.count = count;
        runCounter = 0;
        lastRun = System.currentTimeMillis();
    }

    public abstract NCallback getCallback();
    public abstract String toString();

    public ReqType getType(){
        return type;
    }

    public long getInterval() {
        return interval;
    }

    public boolean isExpired() {
        if(count == -1) {
            return false;
        }
       return runCounter > count;
    }

    public void onExecute() {
        lastRun = System.currentTimeMillis();
        if(count != -1)
            runCounter++;
    }


    public long getScore() {
        return lastRun + interval;
    }

    public boolean isReady() {
        //When interval == 0, its always ready to run
        //when count == 0, it will run right away once and then wait for interval time
        return (interval == 0)|| count == 0 || ((System.currentTimeMillis() - lastRun) > interval);
    }

    public long getWaitTime() {
        if(interval == 0){
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        if(lastRun+interval <= currentTime){
            return 10;
        } else {
            return interval - (currentTime - lastRun);
        }
    }

    public String getRequestId(){
        return REQUEST_ID;
    }


    @Override
    public int compareTo(@NonNull NetRequest o) {
        if(getScore() > o.getScore()){
            return 1;
        } else if(getScore() < o.getScore()){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null){
            if(obj instanceof NetRequest){
                NetRequest other = (NetRequest) obj;
                return this.getRequestId().equals(other.getRequestId());
            }
        }
        return false;
    }
}
