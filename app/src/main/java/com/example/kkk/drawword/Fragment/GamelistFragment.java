package com.example.kkk.drawword.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.RoomActivity;
import com.example.kkk.drawword.Adapter.GamelistAdapter;
import com.example.kkk.drawword.Data.GamelistData;
import com.example.kkk.drawword.GameListGet;
import com.example.kkk.drawword.Okhttp.OkhttpGame;
import com.example.kkk.drawword.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class GamelistFragment extends Fragment implements View.OnClickListener ,AbsListView.OnScrollListener {

    ArrayList<GamelistData> item = new ArrayList<GamelistData>();
    GamelistAdapter gamelistadapter;
    boolean refreash = true;
    @BindView(R.id.create_room) Button create_room;
    @BindView(R.id.game_list) ListView listView;
//    @BindView(R.id.refresh) ImageButton refresh;
    @BindView(R.id.progressbar) ProgressBar progressBar;

    String id, iden, refresh_json, room_name, room_con,check_json;
    String status;
    String refresh_json_before = "";
    int room_iden,room_num,game_list_facus;
    boolean refresh_stop = false;
    private boolean lastItemVisibleFlag = false;
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;
    private boolean mLockListView;
    boolean enter_in_gameroom = true;
    ArrayList<String> game_list_json_array;
    GameListGet gameListGet = GameListGet.getInstance();

    public GamelistFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamelist_fragment,container,false);
        ButterKnife.bind(this,view);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swype_layout);
        progressBar.setVisibility(View.GONE);
        id = getArguments().getString("id");
        iden = getArguments().getString("iden");
        game_list_json_array = new ArrayList<>();
        //리스트 구현
        gamelistadapter = new GamelistAdapter(inflater,getActivity(),item);
//        item.add(new GamelistData(1,"테스트방입니다","2/4"));
        listView.setOnScrollListener(this);
        listView.setAdapter(gamelistadapter);

        //리스트 아이템
        check_json = getArguments().getString("game_list_json");
        game_list_json_array.add(check_json);
        game_list_facus = getArguments().getInt("game_list_focus");

//        getOkhttp("2","0");
        GetJson(GameListGet.getGameList() ,GameListGet.getFocus());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_pos) {
                OkhttpGame okhttpGame = new OkhttpGame();
                room_num = item.get(position).getRoom_num();
                Log.d("aaa","aa");
                try {
                    status = okhttpGame.execute("3",room_num).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getActivity(),status, Toast.LENGTH_SHORT).show();
                if ((status.equals("pass")) && (enter_in_gameroom == true)) {
                    String room_num = String.valueOf(item.get(position).getRoom_num());
                    Toast.makeText(getActivity(), "방에 들어갑니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), RoomActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("iden", iden);
                    intent.putExtra("room_num", room_num);
                    intent.putExtra("room_name", item.get(position).getRoom_name());
                    startActivity(intent);
                    enter_in_gameroom = false;
                    new Overlap_enter_room().start();
                }
                else if (status.equals("nothing")){
                    Toast.makeText(getActivity(), "이미 게임을 시작하고있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //버튼
        create_room.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                GetJson(refresh_json,-1);
                getOkhttp("0","1");
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;

    }



    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "onresume", Toast.LENGTH_SHORT).show();
        getOkhttp("0","1");
//        enter_in_gameroom = true;
        Toast.makeText(getActivity(), "resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true

        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            int list_length = item.size();
//            item.get(list_length-1).getRoom_num();
            getOkhttp("4",String.valueOf(item.get(list_length-1).getRoom_num()));

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        refresh_stop = true;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        refreash = false;
        gameListGet.setFocus(listView.getFirstVisiblePosition());
        Log.d("add_json_fragment",refresh_json_before);
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
                        if (enter_in_gameroom == true) {
                            String room_name = room.getText().toString();
                            String room_iden = null;
                            OkhttpGame okhttpGame = new OkhttpGame();
                            try {
                                room_iden = okhttpGame.execute("1", room_name).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), RoomActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("iden", iden);
                            intent.putExtra("room_num", room_iden);
                            intent.putExtra("room_name", room_name);
                            startActivity(intent);
                            enter_in_gameroom = false;
                            new Overlap_enter_room().start();
                        }
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
            /*case R.id.refresh :
//                getOkhttp("2");
                int pos = listView.getFirstVisiblePosition();
                Toast.makeText(getActivity(), ""+pos, Toast.LENGTH_SHORT).show();
                listView.setSelection(40);*/
        }
    }

    void getOkhttp(String choice,String limit){

        try {
            refresh_json = new OkhttpGame().execute(choice,limit).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        Log.d("http_output",refresh_json);
        if (choice.equals("0")){
            GetJsonRefresh(refresh_json);
        }
        else
            GetJson(refresh_json,-1);

        gameListGet.addGameList(refresh_json);
        /*int idx = 0;
        if (refresh_json_before.contains("]")){
            idx = refresh_json_before.indexOf("]");
            refresh_json_before = refresh_json_before.substring(0,idx);
        }
        if (!refresh_json.equals("nothing")){
            idx = refresh_json.indexOf("[");
            refresh_json = refresh_json.substring(idx+1);
        }
        refresh_json_before = refresh_json_before +","+ refresh_json;*/
        Log.d("json add", refresh_json_before);

        progressBar.setVisibility(View.GONE);

    }
    void GetJsonRefresh(String json_list){
        item.clear();
        gameListGet.resetGameList();
        gameListGet.resetFouce();
        if (json_list.equals("nothing")){
            Log.d("test","return");
            Toast.makeText(getActivity(), "게임 방이 없습니다.", Toast.LENGTH_SHORT).show();
            gamelistadapter.notifyDataSetChanged();
            return;
        }
        try {

            gamelistadapter.notifyDataSetChanged();
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

    void GetJson(ArrayList<String> json_array, int focus){
        if (json_array.get(0).equals("nothing")){
            Log.d("test","return");
            Toast.makeText(getActivity(), "더이상 게임 방이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            for (int j = 0; j < json_array.size(); j++) {
                JSONArray json = new JSONArray(json_array.get(j));
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
            }

            if (focus != -1){
                listView.setSelection(gameListGet.getFocus());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void GetJson(String json_list,int focus){

        /*if (!json_list.equals(null)) {
            Log.d("refresh", json_list);
        }*/
        if (json_list.equals("nothing")){
            Log.d("test","return");
            Toast.makeText(getActivity(), "더 이상 게임 방이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            gamelistadapter.notifyDataSetChanged();
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
            if (focus != -1){
                listView.setSelection(focus);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Overlap_enter_room extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enter_in_gameroom = true;
        }
    }

}



