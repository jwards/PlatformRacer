package coms486.jsward.platformracer.network;

import android.support.annotation.NonNull;

import jsward.platformracer.common.network.ReqType;

public class SimpleRequest extends NetRequest {

    private RequestStatusCallback callback;
    private int extra;

    public SimpleRequest(ReqType type,RequestStatusCallback callback){
        this(type,callback,0,0);

    }

    public SimpleRequest(ReqType type,RequestStatusCallback callback,long interval,int count){
        super(type, interval, count);
        this.callback = callback;
    }



    public void addExtra(int extra){
        this.extra = extra;
    }

    public int getExtra(){
        return extra;
    }

    @Override
    public RequestStatusCallback getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "["+type+", "+callback.toString()+", "+super.getInterval()+"]";
    }
}
