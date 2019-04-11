package coms486.jsward.platformracer.network;

import jsward.platformracer.common.network.ReqType;

public class NetworkManager {

    private static NetworkManager INSTANCE = null;
    private NetworkThread networkThread;

    private NetworkManager(){
        networkThread = new NetworkThread(this);
        networkThread.start();
    }

    public synchronized static NetworkManager getInstance() {
        if(INSTANCE == null){
            INSTANCE = new NetworkManager();
        }
        return INSTANCE;
    }

    public boolean reqLobbyList(LobbyReqCallback callback){
        LobbyUpdateRequest request = new LobbyUpdateRequest(ReqType.REQ_LOBBY_LIST,callback);
        return networkThread.request(request);
    }

    public boolean reqJoinGame(RequestStatusCallback callback, int gameSessionId){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_JOIN, callback).setExtra(gameSessionId);
        return networkThread.request(request);
    }

    public boolean reqCreateGame(RequestStatusCallback callback){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_CREATE,callback);
        return networkThread.request(request);
    }

    public boolean available(){
        return networkThread.isConnected();
    }

}
