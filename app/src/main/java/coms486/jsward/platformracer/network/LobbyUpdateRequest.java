package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public class LobbyUpdateRequest implements NetRequest {

    private ReqType type;
    private LobbyReqCallback callback;

    public LobbyUpdateRequest(ReqType type, LobbyReqCallback responseCallback) {
        this.type = type;
        this.callback = responseCallback;
    }

    @Override
    public ReqType getType() {
        return type;
    }

    @Override
    public LobbyReqCallback getCallback() {
        return callback;
    }
}
