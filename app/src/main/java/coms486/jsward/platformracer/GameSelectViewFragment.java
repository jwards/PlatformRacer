package coms486.jsward.platformracer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import coms486.jsward.platformracer.R;


public class GameSelectViewFragment extends Fragment {


    private Button singlePlayerBtn;
    private Button multiPlayerBtn;

    private FrameLayout lobbyFrame;

    private Fragment currentFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.game_mode_fragment,container,false);

        singlePlayerBtn = view.findViewById(R.id.game_mode_sp_btn);
        multiPlayerBtn = view.findViewById(R.id.game_mode_mp_btn);
        lobbyFrame = view.findViewById(R.id.game_mode_lobby_frame);

        singlePlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        multiPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





        return view;
    }


}
