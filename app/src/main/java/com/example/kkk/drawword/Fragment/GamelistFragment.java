package com.example.kkk.drawword.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.RoomActivity;
import com.example.kkk.drawword.Adapter.GamelistAdapter;
import com.example.kkk.drawword.Data.GamelistData;
import com.example.kkk.drawword.Okhttp.OkhttpGame;
import com.example.kkk.drawword.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class GamelistFragment extends Fragment implements View.OnClickListener {

    ArrayList<GamelistData> item = new ArrayList<GamelistData>();
    GamelistAdapter gamelistadapter;

    @BindView(R.id.create_room) Button create_room;
    @BindView(R.id.game_list) ListView listView;
    @BindView(R.id.refresh) ImageButton refresh;
    String id, iden, refresh_json, room_name, room_con;
    int room_iden;
    boolean refresh_stop = false;
    public GamelistFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamelist_fragment,container,false);
        ButterKnife.bind(this,view);

        id = getArguments().getString("id");
        iden = getArguments().getString("iden");

        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();

        //리스트 구현
        gamelistadapter = new GamelistAdapter(inflater,getActivity(),item);
        item.add(new GamelistData(1,"테스트방입니다","2/4"));
        listView.setAdapter(gamelistadapter);

        //리스트 아이템
        getOkhttp("2");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_pos) {
                Intent intent = new Intent(getActivity(),RoomActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("iden",iden);
                intent.putExtra("room_num",item.get(position).getRoom_num());
                intent.putExtra("room_name",item.get(position).getRoom_name());
                startActivity(intent);
            }
        });

        //버튼
        create_room.setOnClickListener(this);
        refresh.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                getOkhttp("2");
            }
        };

        Thread checkUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(50000);
                        if (refresh_stop == true){
                            break;
                        }
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        checkUpdate.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        refresh_stop = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_room :
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_friend_dialog, null);
                final EditText room= (EditText)dialogView.findViewById(R.id.friend_name);
                TextView title = (TextView) dialogView.findViewById(R.id.ment);
                title.setText("방의 이름을 적어주세요");
                final AlertDialog.Builder buider= new AlertDialog.Builder(getActivity());
                buider.setTitle("방 만들기"); //Dialog 제목
                buider.setView(dialogView);
                buider.setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String room_name = room.getText().toString();
                        OkhttpGame okhttpGame = new OkhttpGame();
                        okhttpGame.execute("1",room_name);
                    }
                });
                buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                buider.show();
                break;
            case R.id.refresh :
                getOkhttp("2");
        }
    }

    void getOkhttp(String choice){
        try {
            refresh_json = new OkhttpGame().execute(choice).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        GetJson(refresh_json);
    }

    void GetJson(String json_list){
        item.clear();
        gamelistadapter.notifyDataSetChanged();
        Log.d("refresh", json_list);
        if (json_list.equals("nothing")){
            Log.d("test","return");
            Toast.makeText(getActivity(), "게임 방이 없습니다. 방을 만들어주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray json = new JSONArray(json_list);
            Log.d("json_length", String.valueOf(json.length()));
            int real_iden = 0;
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                room_con = jsonObject.getString("room_status");
                room_name = jsonObject.getString("room_name");
                room_iden = jsonObject.getInt("iden");
                real_iden = room_iden % 1000;

                item.add(new GamelistData(real_iden,room_name,room_con));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



