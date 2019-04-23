package coms486.jsward.platformracer.ui;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import coms486.jsward.platformracer.GameActivity;
import coms486.jsward.platformracer.GameSelectActivity;
import coms486.jsward.platformracer.network.LobbyReqCallback;
import coms486.jsward.platformracer.network.NetworkManager;
import coms486.jsward.platformracer.network.RequestStatusCallback;
import jsward.platformracer.common.network.GameSessionInfo;
import jsward.platformracer.common.network.ReqType;
import jsward.platformracer.common.network.Status;

public class GameSelectController implements RequestStatusCallback, LobbyReqCallback {


    private GameSelectActivity gameSelectActivity;
    private GameSelectViewFragment gameSelectView;

    private NetworkManager netman = NetworkManager.getInstance();

    private String lobbyUpdateRequestId;

    private boolean showLobby;

    public GameSelectController(GameSelectActivity gameSelectActivity, GameSelectViewFragment gameSelectView){
        this.gameSelectActivity = gameSelectActivity;
        this.gameSelectView = gameSelectView;
        //poll server every 3 seconds for lobby update
        lobbyUpdateRequestId = netman.reqLobbyListRecurring(this,-1,3000);
        showLobby = true;
    }





    public void onSinglePlayerClick(){
        Intent intent = new Intent(gameSelectActivity, GameActivity.class);
        intent.putExtra(GameActivity.FLAG_SINGLEPLAYER,true);
        gameSelectActivity.startActivity(intent);
        gameSelectActivity.finish();
    }

    public void onCreateGameClick(){
        netman.reqCreateGame(this);
    }

    public void onJoinGameClick(int id){
        netman.reqJoinGame(this, id);
    }


    @Override
    public void onLobbyUpdateReceived(ArrayList<GameSessionInfo> lobby) {
        gameSelectView.getLobbyListAdapter().changeData(lobby);
        if(showLobby) {
            gameSelectView.showLobbyView();
        }
    }

    @Override
    public void onSingleLobbyUpdateReceived(GameSessionInfo sessionInfo) {
        if(sessionInfo != null){
            gameSelectView.getGameSessionListAdapter().changeData(sessionInfo);
            gameSelectView.showSessionView();
        }
    }


    @Override
    public void onResponse(ReqType type, Status response,int extra) {
        switch (type){
            case REQ_JOIN:
                //change view to show the joined game or synchronize and start game
                if (response == Status.OK) {
                    //joined game, now request the lobby info and update the view
                    netman.cancleRequest(lobbyUpdateRequestId);
                    showLobby = false;
                    netman.reqLobbyList(this, extra);
                    lobbyUpdateRequestId = netman.reqLobbyListRecurring(this, extra, 1000);
                } else {

                }
                break;
            case REQ_CREATE:
                //change view to show new lobby
                if (response == Status.OK) {
                    //created game, now request the lobby info to display
                    netman.cancleRequest(lobbyUpdateRequestId);
                    showLobby = false;
                    netman.reqLobbyList(this, extra);
                    lobbyUpdateRequestId = netman.reqLobbyListRecurring(this, extra, 1000);
                }
                break;
            case REQ_LOBBY_LIST:

                break;
            case REQ_DESTROY:
                if(response == Status.OK){
                    //we left the game
                    showLobby = true;
                    //cancle the requests for the lobby we were in
                    netman.cancleRequest(lobbyUpdateRequestId);
                    //new request for getting list of lobbies
                    lobbyUpdateRequestId = netman.reqLobbyListRecurring(this, -1, 3000);
                }
                break;
            case REQ_START:
                //enter the game activity

                break;
        }
    }

    public void onRequestStartGame(){
        //send request to server to start the current game
        //first check if the request should be made (i.e. are we actually in a lobby)
        if(!showLobby){
            netman.reqStartGame(this);
        }
    }

    //returns true if the back pressed was handled
    public boolean onBackPressed(){


        if(!showLobby){
            //if we are not in the lobby select menu exit lobby
            //tell server we left the game lobby
            netman.reqLeaveGame(this);
            return true;
        } else {
            //exit lobby menu
            netman.cancleAll();
            return false;
        }
    }
}
