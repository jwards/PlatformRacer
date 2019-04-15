package coms486.jsward.platformracer.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coms486.jsward.platformracer.R;
import coms486.jsward.platformracer.network.LobbyReqCallback;
import jsward.platformracer.common.network.GameSessionInfo;

public class LobbyListAdapter extends BaseAdapter {

    private Context context;
    private GameSelectController actionCallback;
    private ArrayList<GameSessionInfo> lobby;

    private LobbyClickListener clickListener;


    public LobbyListAdapter(Context context){
        this.context = context;
        clickListener = new LobbyClickListener();
        lobby = new ArrayList<>();
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
            name.setText("Empty");
        } else {
            name.setText(gameSessionInfo.lobbyId +" : "+gameSessionInfo.hostPlayerId + " : "+gameSessionInfo.capacity+"/"+gameSessionInfo.maxCapacity);
        }
        view.setTag(position);
        return view;
    }

    public void changeData(ArrayList<GameSessionInfo> lobby) {
        this.lobby = lobby;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void setGameSelectController(GameSelectController gameSelectController){
        this.actionCallback = gameSelectController;
    }

    private class LobbyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();

            GameSessionInfo info = (GameSessionInfo) getItem(tag);
            if(info != null){
                //attempt to join game
                actionCallback.onJoinGameClick(info.lobbyId);
            }
        }
    }
}
