package coms486.jsward.platformracer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coms486.jsward.platformracer.network.NetworkResponseCallback;
import jsward.platformracer.common.network.GameSessionInfo;
import jsward.platformracer.common.network.Status;

public class LobbyListAdapter extends BaseAdapter implements NetworkResponseCallback {

    private Context context;
    private ArrayList<GameSessionInfo> lobby;

    public LobbyListAdapter(Context context){
        this.context = context;
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
        TextView temp;
        if(convertView == null){
            temp = new TextView(context);
        } else {
            temp = (TextView) convertView;
        }
        temp.setText(getItem(position).toString());
        return temp;
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

    @Override
    public void onResponse(Status status) {
        //do nothing
    }
}
