package coms486.jsward.platformracer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import coms486.jsward.platformracer.GameActivity;
import coms486.jsward.platformracer.R;
import coms486.jsward.platformracer.network.NetworkManager;


public class GameSelectViewFragment extends Fragment {

    private static final String DEBUG_TAG = "GAME_SELECT_FRAGMENT";


    private Button singlePlayerBtn;
    private Button multiPlayerBtn;
    private ListView lobbyListView;

    private ListAdapter currentAdapter;
    private LobbyListAdapter lobbyListAdapter;
    private GameSessionListAdapter gameSessionListAdapter;

    private GameSelectController controller;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.game_mode_fragment,container,false);

        singlePlayerBtn = view.findViewById(R.id.game_mode_sp_btn);
        multiPlayerBtn = view.findViewById(R.id.game_mode_mp_btn);

        lobbyListView = view.findViewById(R.id.game_lobby_list);
        lobbyListAdapter = new LobbyListAdapter(getContext());
        gameSessionListAdapter = new GameSessionListAdapter(getContext());
        lobbyListView.setAdapter(lobbyListAdapter);
        currentAdapter = lobbyListAdapter;

        singlePlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller!=null){
                    controller.onSinglePlayerClick();
                }
            }
        });

        multiPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller != null){
                     controller.onCreateGameClick();
                }
            }
        });
        return view;
    }

    public LobbyListAdapter getLobbyListAdapter(){
        return lobbyListAdapter;
    }

    public GameSessionListAdapter getGameSessionListAdapter(){
        return gameSessionListAdapter;
    }

    public void showLobbyView(){
        setListViewAdapter(lobbyListAdapter);
    }

    public void showSessionView(){
        setListViewAdapter(gameSessionListAdapter);
    }

    public void setController(GameSelectController controller){
        this.controller = controller;
        lobbyListAdapter.setGameSelectController(controller);
    }

    private void setListViewAdapter(final ListAdapter adapter){
        if(currentAdapter == adapter){
            return;
        }

        currentAdapter = adapter;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                lobbyListView.setAdapter(adapter);
            }
        });
    }



}
