package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public class LobbyUpdateRequest extends NetRequest {

    private LobbyReqCallback callback;

    private int lobbyId;

    public LobbyUpdateRequest(ReqType type,int lobbyId,LobbyReqCallback callback,long millis,int count){
        super(type,millis,count);
        this.callback = callback;
        this.lobbyId = lobbyId;
    }

    public LobbyUpdateRequest(ReqType type,int lobbyId ,LobbyReqCallback callback) {
        this(type,lobbyId,callback,0,0);
    }

    public int getLobbyId(){
        return lobbyId;
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
