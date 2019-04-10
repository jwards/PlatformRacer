package coms486.jsward.platformracer.network;

import java.util.ArrayList;

import jsward.platformracer.common.network.GameSessionInfo;
import jsward.platformracer.common.network.Status;

public interface NetworkResponseCallback {

    void onLobbyUpdated(ArrayList<GameSessionInfo> lobby);
    void onResponse(Status status);

}
