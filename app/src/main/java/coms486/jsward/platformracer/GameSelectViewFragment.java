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
import android.widget.Toast;

import coms486.jsward.platformracer.R;
import coms486.jsward.platformracer.network.NetworkManager;


public class GameSelectViewFragment extends Fragment {

    private static final String DEBUG_TAG = "GAME_SELECT_FRAGMENT";


    private Button singlePlayerBtn;
    private Button multiPlayerBtn;


    private Fragment currentFragment;

    private NetworkManager networkManager;

    private LobbyViewFragment lobbyViewFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.game_mode_fragment,container,false);

        singlePlayerBtn = view.findViewById(R.id.game_mode_sp_btn);
        multiPlayerBtn = view.findViewById(R.id.game_mode_mp_btn);

        lobbyViewFragment = (LobbyViewFragment) getChildFragmentManager().findFragmentById(R.id.fragment_lobby_view);

        if(lobbyViewFragment == null){
            Log.e(DEBUG_TAG, "Couldn't find lobby view fragment");
        }

        if(networkManager == null){
            Log.e(DEBUG_TAG, "Error: Network Manager is null.");
        }

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
                if(networkManager.available()){
                    networkManager.reqLobbyList(lobbyViewFragment.getListAdapter());
                } else {
                    Toast.makeText(getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setNetworkManager(NetworkManager networkManager){
        this.networkManager = networkManager;
    }

}
