package coms486.jsward.platformracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import coms486.jsward.platformracer.R;
import coms486.jsward.platformracer.network.NetworkManager;
import coms486.jsward.platformracer.network.NetworkThread;
import coms486.jsward.platformracer.network.RequestStatusCallback;
import jsward.platformracer.common.network.ReqType;
import jsward.platformracer.common.network.Status;


public class GameSelectViewFragment extends Fragment implements RequestStatusCallback {

    private static final String DEBUG_TAG = "GAME_SELECT_FRAGMENT";


    private Button singlePlayerBtn;
    private Button multiPlayerBtn;
    private ListView lobbyListView;

    private LobbyListAdapter lobbyListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.game_mode_fragment,container,false);

        singlePlayerBtn = view.findViewById(R.id.game_mode_sp_btn);
        multiPlayerBtn = view.findViewById(R.id.game_mode_mp_btn);

        lobbyListView = view.findViewById(R.id.game_lobby_list);
        lobbyListAdapter = new LobbyListAdapter(getContext(),this);
        lobbyListView.setAdapter(lobbyListAdapter);


        singlePlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch GameActivity as single player
                Intent intent = new Intent(getActivity(),GameActivity.class);
                intent.putExtra(GameActivity.FLAG_SINGLEPLAYER,true);
                startActivity(intent);
                getActivity().finish();
            }
        });

        multiPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create game lobby
                NetworkManager netman = NetworkManager.getInstance();
                if(netman.available()){
                    netman.reqCreateGame(null);
                } else {
                    Toast.makeText(getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        NetworkManager netman = NetworkManager.getInstance();
        //poll server every 3 seconds for lobby update
        netman.reqLobbyListRecurring(lobbyListAdapter,3000);

        return view;
    }


    public void joinGame(int id){
        NetworkManager netman = NetworkManager.getInstance();
        netman.reqJoinGame(this, id);
    }


    @Override
    public void onResponse(ReqType type, Status response) {
        switch (type) {
            case REQ_JOIN:
                if(response == Status.BEGIN){
                    //game is begining

                } else if (response == Status.OK){
                    //joined game but game hasn't begun yet

                } else{
                    //bad request

                }
        }
    }
}
