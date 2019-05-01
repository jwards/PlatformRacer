package coms486.jsward.platformracer.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
            if(sessionInfo.isHost(sessionInfo.hostPlayer.getId())) {
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
            return sessionInfo.players.get(position).getName();
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
           // view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(clickLisener);
        } else {
            view = convertView;
        }

        TextView tv = view.findViewById(R.id.lobby_entry_text);

        if(position == 0){
            lobbyTitleCard(tv);
            view.setTag(TITLE_CARD_TAG);
        } else if(position == 1){
            hostCard(tv);
            view.setTag(HOST_CARD_TAG);
        } else {
            //otherwise just display a player
            lobbyPlayerEntry(tv, position - 2);
            view.setTag(position);
        }
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

    private void lobbyTitleCard(TextView tv){
        tv.setTextColor(Color.BLUE);
        tv.setText("Capacity: " + sessionInfo.capacity + " / " + sessionInfo.maxCapacity);
    }

    private void lobbyPlayerEntry(TextView name,int playerPosition){
        String playerName = (String) getItem(playerPosition);
        if(playerName == null){
            name.setText("Empty");
        } else {
            if(playerName.equals(sessionInfo.hostPlayer.getName())){
                name.setTextColor(Color.RED);
            } else {
                name.setTextColor(Color.DKGRAY);
            }
            name.setText(playerName);
        }
    }

    private void hostCard(TextView tv){
        if(sessionInfo.ready()) {
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(Color.RED);
        }
        tv.setText("START GAME");
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
