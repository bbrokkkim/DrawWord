package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Adapter.ReadyAdapter;
import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.Data.ReadyData;
import com.example.kkk.drawword.Okhttp.TcpChat;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.Adapter.RoomAdapter;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Tcp_chat;
import com.example.kkk.drawword.Tcp_connect;
import com.example.kkk.drawword.Test2Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */
public class RoomActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.fl_activity_main_container) FrameLayout frameLayout;
    @BindView(R.id.text_ment) EditText text;
    @BindView(R.id.ment_view) ListView listView;
    @BindView(R.id.ready_list) ListView readylist;
    @BindView(R.id.room_name) TextView roomname;
    @BindView(R.id.ready_btn) Button ready_btn;
    @BindView(R.id.my_ready) TextView my_ready;
    @BindView(R.id.back_btn_game) ImageButton back_btn;
    @BindView(R.id.open_navigation) ImageButton open_navigation;
    @BindView(R.id.navigation) LinearLayout naviation;
    @BindView(R.id.dl_activity_main_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.submit_ment) Button submit;


    ArrayList<ChatData> item= new ArrayList();
    ArrayList<ReadyData> item_ready = new ArrayList<>();
    ArrayList<String> ready_list;

    RoomAdapter room_adapter;
    ReadyAdapter readyAdapter;


    String to,ment;
    String content;
    Socket socket;
    String push_content;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    Tcp_chat tcp_chat;
    boolean ready = true;
    String id,iden,room_name,room_num;

    SocketGet socketGet = SocketGet.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        layout();
        ButterKnife.bind(this);
        Intent get = getIntent();

        //정보 가지고오기
        id = get.getStringExtra("id");
        iden = get.getStringExtra("iden");
        room_name = get.getStringExtra("room_name");
        int asdf = get.getIntExtra("room_num",1);
        room_num = String.valueOf(asdf);
        roomname.setText(room_num + ".  " + room_name);
        Log.d("asdfas",id);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(room_adapter);
        readylist.setAdapter(readyAdapter);

        my_ready.setText(id);

        try {
            String test = new Tcp_connect(this).execute("8000",room_num + "《" + id).get();
            Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
            checkUpdate.start();
            Log.d("test","thread1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        submit.setOnClickListener(this);
        open_navigation.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        ready_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //채팅문자전송
            case R.id.submit_ment:
                String chat_content = text.getText().toString();
                push_content = "1《" + room_num + "《" + id + "》" + chat_content;
                new Tcp_chat().execute(id, push_content);
                break;
            //네비게이션드로어 열기
            case R.id.open_navigation:
                drawerLayout.openDrawer(naviation);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
/*
                Intent intenta = new Intent(RoomActivity.this,Test2Activity.class);
                startActivity(intenta);
*/
                break;
            //채팅방 나오기
            case R.id.back_btn_game :
                tcp_chat = new Tcp_chat();
                tcp_chat.execute(id ,"10《" + room_num + "《" + id + "》");
                finish();
                break;
            //게임준비하기
            case R.id.ready_btn :
                String ready_content;
                if (ready == true){
                    ready_content = "wait";
                    ready_btn.setText("wait");
                    ready = false;
                }
                else {
                    ready_content = "ready";
                    ready_btn.setText("ready");
                    ready = true;
                }
                Toast.makeText(RoomActivity.this, ready_content, Toast.LENGTH_SHORT).show();
                Toast.makeText(RoomActivity.this, id, Toast.LENGTH_SHORT).show();
                push_content = "2《" + room_num + "《" + id + "》" + ready_content;
                new Tcp_chat().execute(id ,push_content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (socketGet.getBufferedReader() != null){
            Toast.makeText(this, "adsf", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "bbbb", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tcp_chat = new Tcp_chat();
        tcp_chat.execute(id ,"10《" + room_num + "《" + id + "》");
    }


    Thread checkUpdate = new Thread() {

        public void run() {
            String line = null;
            Log.w("ChattingStart", "Start Thread");
            String tcp_type = null;
            content = null;
            boolean test = false;
            try {
                while ((line = socketGet.getBufferedReader().readLine()) != null) {
                    Log.d("line", line);
                    if (!line.contains("《") || !line.contains("》")){
                        Log.w("Chatting is error" , "error");
                        continue;
                    }

                    Log.w("Chatting is running" , "1");

                    int idx_ment = line.indexOf("《");
                    String real_ment = line.substring(idx_ment + 1);
                    Log.d("made_line", real_ment);
                    int idx = real_ment.indexOf("《");
                    Log.d("chatting Test", String.valueOf(idx));
                    tcp_type = real_ment.substring(0, idx);
                    content = real_ment.substring(idx+1);
                    Log.d("Chatting is content", content);
                    Log.d("Chatting is tcp_type", tcp_type);


                    /*switch (tcp_type){
                        case "1":
                            idx = content.indexOf("《");
                            content = content.substring(idx + 1);
                            idx = content.indexOf("》");
                            to = content.substring(0,idx);
                            ment = content.substring(idx+1);
                            chatting.sendEmptyMessage(0);
                            Log.d("Chatting is to", to);
                            Log.d("Chatting is ment", ment);
                            break;
                        case "2":
                            int test_int = 1;
                            ready_list = new ArrayList<>();
                            while (true){
                                if (content.contains("《")){
                                    idx = content.indexOf("《");
                                    ready_list.add(content.substring(0,idx));
                                    Log.d("list"+test_int,content.substring(0,idx));
                                    content = content.substring(idx+1);
                                }
                                else {
                                    ready_list.add(content);
                                    Log.d("last"+test_int,content);
                                    break;
                                }
                                test_int = test_int + 1;
                            }
                            user_list_status.sendEmptyMessage(0);
                            break;
                        case "5":
                            all_start.sendEmptyMessage(0);
                            break;
                            break;
                    }*/

                    //chatting
                    if (tcp_type.equals("1")){
                        idx = content.indexOf("《");
                        content = content.substring(idx + 1);

                        idx = content.indexOf("》");
                        to = content.substring(0,idx);
                        ment = content.substring(idx+1);
                        chatting.sendEmptyMessage(0);
                        Log.d("Chatting is to", to);
                        Log.d("Chatting is ment", ment);
                    }

                    //user_list_status
                    else if (tcp_type.equals("2")){
                        int test_int = 1;
                        ready_list = new ArrayList<>();
                        while (true){
                            if (content.contains("《")){
                                idx = content.indexOf("《");
                                ready_list.add(content.substring(0,idx));
                                Log.d("list"+test_int,content.substring(0,idx));
                                content = content.substring(idx+1);
                            }
                            else {
//                                ready_list.add(content);
                                Log.d("last"+test_int,content);
                                break;
                            }
                            test_int = test_int + 1;

                        }
                        Log.d("last"+test_int,content);
                        user_list_status.sendEmptyMessage(0);
                    }
                    else if (tcp_type.equals("2.5")){
                        all_start.sendEmptyMessage(0);
                        break;
                    }
                    else if (tcp_type.equals("6.5")){
                        master_start.sendEmptyMessage(0);
                        break;
                    }


//                    ment = real_ment.substring(idx + 1);


                    Log.d("Chatting is running", "2");


                }
                Log.d("Chatting is running", "21");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Chatting is running", "2111");

        }
    };

    Handler chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));

            item.add(new ChatData(to,"  " + ment + "  "));
            room_adapter.notifyDataSetChanged();

        }
    };


    Handler all_start = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int idx;
            String other_id,other_status;
            /*item_ready.clear();
            for (int i = 0; i < ready_list.size(); i++) {
                idx = ready_list.get(i).indexOf("》");
                other_id = ready_list.get(i).substring(0,idx);
                other_status = ready_list.get(i).substring(idx+1);
                Log.d("status",other_status);
                item_ready.add(new ReadyData(other_id,other_status));
            }
            readyAdapter.notifyDataSetChanged();*/
            Toast.makeText(RoomActivity.this, "시작합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RoomActivity.this,DrawActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("room_num",room_num);
            intent.putExtra("status","2");
            startActivity(intent);
        }
    };
    Handler master_start = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(RoomActivity.this, "방장으로 시작합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RoomActivity.this,DrawActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("room_num",room_num);
            intent.putExtra("status","1");
            startActivity(intent);
        }
    };


    Handler user_list_status = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int idx;
            String other_id,other_status;
            item_ready.clear();
//            Log.d("check",ready_list.get(0));
            for (int i = 0; i < ready_list.size(); i++) {
//                Log.d("check",ready_list.get(i));
                idx = ready_list.get(i).indexOf("》");
                other_id = ready_list.get(i).substring(0,idx);
                other_status = ready_list.get(i).substring(idx+1);
                Log.d("status_",other_status);
                item_ready.add(new ReadyData(other_id,other_status));
            }
            readyAdapter.notifyDataSetChanged();

        }
    };





    void layout(){
        room_adapter = new RoomAdapter(getLayoutInflater(),item);
        readyAdapter = new ReadyAdapter(getLayoutInflater(),this,item_ready);
    }
}