package coms486.jsward.platformracer.network;

public class NetRequest {

    private ReqType type;
    private NetworkResponseCallback callback;

    private int joinSessionId;

    public NetRequest(ReqType type,NetworkResponseCallback responseCallback){
        this.type = type;
        this.callback = responseCallback;
    }

    public NetRequest setJoinSessionId(int id){
        joinSessionId = id;
        return this;
    }

    public ReqType getType(){
        return type;
    }

    public NetworkResponseCallback getCallback(){
        return callback;
    }

    public int getJoinSessionId(){
        return joinSessionId;
    }

    public String toString(){
        return "[Type: "+type + ", Callback: "+callback.toString() +"]";
    }
}
