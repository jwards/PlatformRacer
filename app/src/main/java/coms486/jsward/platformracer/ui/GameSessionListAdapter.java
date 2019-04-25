package coms486.jsward.platformracer.ui;

import android.content.Context;
import android.content.res.ColorStateList;
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

    private GameSelectController actionCallback;
    private GameSessionInfo sessionInfo;
    private ClickLisener clickLisener;

    private static final int TITLE_CARD_TAG = -1;
    private static final int HOST_CARD_TAG = -2;


    public GameSessionListAdapter(Context context){
        this.context = context;
        this.clickLisener = new ClickLisener();
    }

    public void setGameSelectController(GameSelectController gameSelectController){
        this.actionCallback = gameSelectController;
    }

    @Override
    public int getCount() {
        if(sessionInfo!=null) {
            int count = sessionInfo.players.size();
            //for title card
            count += 1;
            //if host add one for the host card
            //TODO use real player ID
            if(sessionInfo.isHost(sessionInfo.hostPlayerId)) {
                count+=1;
            }
            return count;
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
            view.setOnClickListener(clickLisener);
        } else {
            view = convertView;
        }

        TextView tv = view.findViewById(R.id.lobby_entry_text);

        if(position == 0){
            return lobbyTitleCard(tv);
        }

        if(position == 1){
            return hostCard(tv);
        }


        //otherwise just display a player
        return lobbyPlayerEntry(tv, position - 2);
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

    private View lobbyTitleCard(View view){
        TextView tv = (TextView) view;
        tv.setTextColor(Color.BLUE);
        tv.setText("Capacity: " + sessionInfo.capacity + " / " + sessionInfo.maxCapacity);

        tv.setTag(TITLE_CARD_TAG);
        return tv;
    }

    private View lobbyPlayerEntry(View view,int playerPosition){
        String playerName = (String) getItem(playerPosition);
        TextView name = (TextView) view;

        if(playerName == null){
            name.setText("Empty");
        } else {
            if(playerPosition== sessionInfo.hostPlayerId){
                name.setTextColor(Color.RED);
            } else {
                name.setTextColor(Color.DKGRAY);
            }
            name.setText(playerName);
        }
        view.setTag(playerPosition);
        return view;
    }

    private View hostCard(View view){
        TextView tv = (TextView) view;
        if(sessionInfo.ready()) {
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(Color.RED);
        }
        tv.setText("START GAME");
        tv.setTag(HOST_CARD_TAG);
        return tv;
    }

    private class ClickLisener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (tag == HOST_CARD_TAG) {
                actionCallback.onRequestStartGame();
            }
        }
    }

}
