package coms486.jsward.platformracer.network;

import android.support.annotation.NonNull;

import jsward.platformracer.common.network.ReqType;

public abstract class NetRequest implements Comparable<NetRequest> {

    protected ReqType type;

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
        return count != -1 && runCounter > count;
    }

    public void onExecute() {
        lastRun = System.currentTimeMillis();
        if(count>0)
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
        return interval - (System.currentTimeMillis() - lastRun);
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
}
