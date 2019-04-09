package coms486.jsward.platformracer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LobbyViewFragment extends Fragment  {

    private LobbyListAdapter listAdapter;

    private ListView lobbyList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game_lobby_fragment, container, false);

        lobbyList = view.findViewById(R.id.game_lobby_list);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
