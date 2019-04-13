package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public class LobbyUpdateRequest extends NetRequest {

    private LobbyReqCallback callback;


    public LobbyUpdateRequest(ReqType type,LobbyReqCallback callback,long millis,int count){
        super(type,millis,count);
        this.callback = callback;
    }

    public LobbyUpdateRequest(ReqType type, LobbyReqCallback callback) {
        this(type,callback,0,0);
    }

    @Override
    public LobbyReqCallback getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "["+type+", "+callback.toString()+", "+super.getInterval()+"]";
    }

}
