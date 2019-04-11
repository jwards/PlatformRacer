package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public class SimpleRequest implements NetRequest {

    private ReqType type;
    private RequestStatusCallback callback;

    private int extra;

    public SimpleRequest(ReqType type,RequestStatusCallback callback){
        this.type = type;
        this.callback = callback;
    }

    public NetRequest addExtra(int extra){
        this.extra = extra;
        return this;
    }

    public ReqType getType(){
        return type;
    }

    public RequestStatusCallback getCallback(){
        return callback;
    }

    public int getExtra(){
        return extra;
    }

    public SimpleRequest setExtra(int extra){
        this.extra = extra;
        return this;
    }

    public String toString(){
        return "[Type: "+type + ", Callback: "+callback.toString() +"]";
    }

}
