package coms486.jsward.platformracer.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coms486.jsward.platformracer.R;
import jsward.platformracer.common.network.GameSessionInfo;


class GameSessionListAdapter extends BaseAdapter {

    private Context context;

    private GameSessionInfo sessionInfo;


    public GameSessionListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if(sessionInfo!=null) {
            return sessionInfo.players.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(sessionInfo != null) {
            return sessionInfo.players.get(position);
        } else {
            return null;
        }
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
        } else {
            view = convertView;
        }

        String playerName = (String) getItem(position);
        TextView name = view.findViewById(R.id.lobby_entry_text);

        if(playerName == null){
            name.setText("Empty");
        } else {
            if(position == sessionInfo.hostPlayerId){
                name.setTextColor(Color.RED);
            } else {
                name.setTextColor(Color.DKGRAY);
            }
            name.setText(playerName+" : "+sessionInfo.capacity+"/"+sessionInfo.maxCapacity);
        }
        view.setTag(position);
        return view;
    }


    public void changeData(GameSessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

}
