package coms486.jsward.platformracer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coms486.jsward.platformracer.network.LobbyReqCallback;
import jsward.platformracer.common.network.GameSessionInfo;
import jsward.platformracer.common.network.Status;

public class LobbyListAdapter extends BaseAdapter implements LobbyReqCallback {

    private Context context;
    private GameSelectViewFragment actionCallback;
    private ArrayList<GameSessionInfo> lobby;

    private LobbyClickListener clickListener;

    public LobbyListAdapter(Context context,GameSelectViewFragment actionCallback){
        this.context = context;
        this.actionCallback = actionCallback;
        clickListener = new LobbyClickListener();
        lobby = new ArrayList<>();
        lobby.add(new GameSessionInfo(-1, "TEST", 0, 0));
    }


    @Override
    public int getCount() {
        return lobby.size();
    }

    @Override
    public Object getItem(int position) {
        return lobby.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = View.inflate(context, R.layout.lobby_list_entry, null);
            view.setOnClickListener(clickListener);
        } else {
            view = convertView;
        }

        GameSessionInfo gameSessionInfo = (GameSessionInfo) getItem(position);
        TextView name = view.findViewById(R.id.lobby_entry_text);
        if(gameSessionInfo == null){

        } else {
            name.setText(gameSessionInfo.lobbyId +" : "+gameSessionInfo.hostPlayerName + " : "+gameSessionInfo.capacity+"/"+gameSessionInfo.maxCapacity);
        }
        view.setTag(position);
        return view;
    }

    @Override
    public void onLobbyUpdated(ArrayList<GameSessionInfo> lobby) {
        this.lobby = lobby;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private class LobbyClickListener implements View.OnClickListener {

        public LobbyClickListener(){

        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();

            GameSessionInfo info = (GameSessionInfo) getItem(tag);
            if(info != null){
                //attempt to join game
                actionCallback.joinGame(info.lobbyId);
            }
        }
    }
}
