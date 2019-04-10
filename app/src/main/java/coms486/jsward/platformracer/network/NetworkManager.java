package coms486.jsward.platformracer.network;

import jsward.platformracer.common.game.GameCore;

public class NetworkManager {

    private NetworkThread networkThread;

    public NetworkManager(){
        networkThread = new NetworkThread(this);
        networkThread.start();
    }

    public boolean reqLobbyList(NetworkResponseCallback callback){
        NetRequest request = new NetRequest(ReqType.REQ_LOBBY_LIST,callback);
        return networkThread.request(request);
    }

    public boolean reqJoinGame(NetworkResponseCallback callback, int gameSessionId){
        NetRequest request = new NetRequest(ReqType.REQ_JOIN, callback).setJoinSessionId(gameSessionId);
        return networkThread.request(request);
    }

    public boolean reqCreateGame(NetworkResponseCallback callback){
        NetRequest request = new NetRequest(ReqType.REQ_CREATE,callback);
        return networkThread.request(request);
    }

    public boolean available(){
        return networkThread.isConnected();
    }

}
