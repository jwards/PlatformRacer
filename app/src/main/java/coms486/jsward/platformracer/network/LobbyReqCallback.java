package coms486.jsward.platformracer.network;

import java.util.ArrayList;

import jsward.platformracer.common.network.GameSessionInfo;
import jsward.platformracer.common.network.Status;

public interface LobbyReqCallback extends NCallback{
    void onLobbyUpdated(ArrayList<GameSessionInfo> lobby);
}
