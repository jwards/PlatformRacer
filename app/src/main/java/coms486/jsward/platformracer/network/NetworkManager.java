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

    public boolean reqLobbyList(LobbyReqCallback callback,int lobbyId){
        LobbyUpdateRequest request = new LobbyUpdateRequest(ReqType.REQ_LOBBY_LIST,lobbyId,callback);
        return networkThread.request(request);
    }

    //LobbyId = -1 to get all lobbies
    //else attempt to get specific lobby
    public String reqLobbyListRecurring(LobbyReqCallback callback,int lobbyId,long millis){
        LobbyUpdateRequest request = new LobbyUpdateRequest(ReqType.REQ_LOBBY_LIST,lobbyId, callback, millis, -1);
        if(networkThread.request(request)){
            return request.getRequestId();
        } else {
            return "";
        }
    }

    public boolean reqJoinGame(RequestStatusCallback callback, int gameSessionId){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_JOIN, callback);
        request.addExtra(gameSessionId);
        return networkThread.request(request);
    }

    public boolean reqLeaveGame(RequestStatusCallback callback){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_DESTROY, callback);
        request.addExtra(-1);
        return networkThread.request(request);
    }

    public boolean reqCreateGame(RequestStatusCallback callback){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_CREATE,callback);
        return networkThread.request(request);
    }

    public boolean reqStartGame(RequestStatusCallback callback){
        SimpleRequest request = new SimpleRequest(ReqType.REQ_START,callback);
        return networkThread.request(request);
    }

    public void cancleRequest(String requestId){
        networkThread.cancleRequest(requestId);
    }

    public void cancleAll(){
        networkThread.cancleAll();
    }

    public boolean available(){
        return networkThread.isConnected();
    }

}
