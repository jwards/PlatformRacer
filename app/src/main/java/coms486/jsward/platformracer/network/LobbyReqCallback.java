package coms486.jsward.platformracer.network;

import java.util.ArrayList;

import jsward.platformracer.common.network.GameSessionInfo;

public interface LobbyReqCallback extends NCallback{
    void onLobbyUpdateReceived(ArrayList<GameSessionInfo> lobby);
    void onSingleLobbyUpdateReceived(GameSessionInfo lobby);
    void onGameStart();

}
