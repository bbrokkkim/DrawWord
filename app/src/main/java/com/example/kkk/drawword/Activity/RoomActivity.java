package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.kkk.drawword.Data.ReadyData;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.Adapter.RoomAdapter;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Okhttp.Tcp_chat;
import com.example.kkk.drawword.Okhttp.Tcp_connect;
//import com.example.kkk.drawword.Test2Activity;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by KKK on 2017-08-17.
 */
public class RoomActivity extends Activity implements View.OnClickListener{
    private UncaughtExceptionHandler androidDefaultUEH;
    private UncaughtExceptionHandler mUncaughtExceptionHandler;
    private UncaughtExceptionHandler unCatchExceptionHandler;

    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return unCatchExceptionHandler;
    }
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
    @BindView(R.id.invate) Button invate;

    ArrayList<ChatData> item= new ArrayList();
    ArrayList<ReadyData> item_ready = new ArrayList<>();
    ArrayList<String> ready_list;
    ArrayList<String> to_array = new ArrayList<>();
    ArrayList<String> ment_array = new ArrayList<>();
    int flow =0;
    RoomAdapter room_adapter;
    ReadyAdapter readyAdapter;

    String to,ment;
    String content;
    Socket socket;
    String push_content;
    String tagger_or_not = "1";
    Tcp_chat tcp_chat;
    boolean exit = false;
    boolean ready = true;
    boolean connect_check_thread = true;
    boolean update_protocal = true;
    String id,iden,room_name,room_num;

    SocketGet socketGet = SocketGet.getInstance();
    Thread a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
//        unCatchExceptionHandler = new UncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(unCatchExceptionHandler);

        super.onCreate(savedInstanceState);
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(mUncaughtExceptionHandler instanceof UncaughtExceptionHandlerApplication)) {
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerApplication());
        }
        setContentView(R.layout.room_layout);
        ButterKnife.bind(this);
        Intent get = getIntent();
        id = get.getStringExtra("id");
        iden = get.getStringExtra("iden");
        room_name = get.getStringExtra("room_name");
        room_num = get.getStringExtra("room_num");
        roomname.setText(room_num + ".  " + room_name);
        Log.d("asdfas",id);

        layout();

        //정보 가지고오기
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(room_adapter);
        readylist.setAdapter(readyAdapter);

        try {
            String test = new Tcp_connect(this).execute("8000",room_num + "《" + id + "《" + tagger_or_not).get();
            checkUpdate.start();
//            new checkUpdate_test().start();
//            checkConnectSocket.start();
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
        invate.setOnClickListener(this);
        Toast.makeText(this, "oncreate", Toast.LENGTH_SHORT).show();
    }

    public class UncaughtExceptionHandlerApplication implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // 이곳에서 로그를 남기는 작업을 하면 된다.
            Log.d("uncaught", "error 123123123123132123 ");
            Toast.makeText(RoomActivity.this, "!!!!!!!!!!!비정상 종료", Toast.LENGTH_SHORT).show();
            /*tcp_chat = new Tcp_chat();
            tcp_chat.execute(id ,"14《" + room_num + "《" + id + "》");*/
            exit = true;
            android.os.Process.killProcess(android.os.Process.myPid());
            Log.d("uncaught", "error -----------------> ");
            System.exit(0);
            Log.d("uncaught", "error -----------------> ");
            //androidDefaultUEH.uncaughtException(thread, ex);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //채팅문자전송
            case R.id.submit_ment:
                String chat_content = text.getText().toString();
                if (!chat_content.equals("")) {
                    push_content = "1《" + room_num + "《" + id + "》" + chat_content;
                    new Tcp_chat().execute(id, push_content);
                    text.setText("");

                }
                break;
                //네비게이션드로어 열기
            case R.id.open_navigation:
                drawerLayout.openDrawer(naviation);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                /*socketGet.disconnectReader();
                socketGet.disconnectWirter();
                socketGet.disconnectSocket();
                */
//                socketGet.disconnectSocket();
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
                push_content = "2《" + room_num + "《" + id + "》" + ready_content;
                new Tcp_chat().execute(id ,push_content);
                break;
            case R.id.invate :
                Intent intent = new Intent(RoomActivity.this,InvateActivity.class);
                intent.putExtra("user_name",id);
                intent.putExtra("room_num",room_num);
                intent.putExtra("room_name",room_name);
                startActivity(intent);
        }
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        try {
            String test = new Tcp_connect(this).execute("8000",room_num + "《" + id).get();
            Log.d("test","thread2");
            checkUpdate.start();
            Log.d("test","thread3");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("interr","thread2");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("execution","thread2");
        }
        tcp_chat = new Tcp_chat();
        tcp_chat.execute(id ,"12《" + room_num + "《" + id + "》");
        Toast.makeText(this, "onrestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tcp_chat = new Tcp_chat();
        try {
            tcp_chat.execute(id ,"11《" + room_num + "《" + id + "》").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "onstop", Toast.LENGTH_SHORT).show();

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "room ondestroy", Toast.LENGTH_SHORT).show();
//        tcp_chat.execute(id ,"15《" + room_num + "《" + id + "》");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tcp_chat = new Tcp_chat();
        tcp_chat.execute(id ,"10《" + room_num + "《" + id + "》");
        exit = true;
    }
    Thread checkConnectSocket = new Thread(){
        public void run(){
            while(true) {
//                Log.d("stream", "서버 연결 확인 쓰레드 시작");
                boolean result = socketGet.getSocket().isConnected() && ! socketGet.getSocket().isClosed();
//                boolean connected = socket.isConnected() && ! socket.isClosed();
//                Log.d("stream", "서버 연결 확인 쓰레드 시작2");
                if (result) {
                    Log.d("stream", "server connect complete~~~~~");
                } else {
                    Log.d("stream", "server connect fail~~~~~");

                    try {
                        Log.d("stream", "server connect room_num" + room_num);

                        String test = new Tcp_connect(RoomActivity.this).execute("8000",room_num + "《" + id + "《" + tagger_or_not).get();
//                        Toast.makeText(RoomActivity.this, room_num, Toast.LENGTH_SHORT).show();
//                        run = true;
//                        checkUpdate.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (connect_check_thread = false){
                    Toast.makeText(RoomActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    break;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("stream", "서버 연결 확인 쓰레드 다시   시작");
                if (connect_check_thread = false){
                    Toast.makeText(RoomActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };
/*
    public class checkUpdate_test extends Thread{
        public checkUpdate_test(){
            Toast.makeText(RoomActivity.this, "aaasdfasdfsdfasdfasdf", Toast.LENGTH_SHORT).show();
            Log.d("iii",String.valueOf("Asdasdasd"));
        }
        @Override
        public void run() {
            int i = 1;
            while(true){
                Toast.makeText(RoomActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                Log.d("iii",String.valueOf(i));
                i = i + 1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler chatting_test = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));
            Log.d("chatting_test",ment_array.get(flow));
            item.add(new ChatData(to_array.get(flow),"  " + ment_array.get(flow) + "  "));
            flow = flow + 1;
            room_adapter.notifyDataSetChanged();

        }
    };*/

    Thread checkUpdate = new Thread() {

        public void run() {
            item.add(new ChatData("test start","  " + "bbbb" + "  "));
            String line = null;
            Log.d("ChattingStart", "Start Thread1111");
            String tcp_type = null;
            content = null;
            boolean test = false;
//            try {
//                while (!socketGet.getSocket().isConnected() && ! socketGet.getSocket().isClosed()) {
//                    Log.d("checksocket",String.valueOf(socketGet.getSocket().isConnected() && ! socketGet.getSocket().isClosed()));

            while (true) {
                Log.d("check","start111111");
                boolean result = socketGet.getSocket().isConnected() && ! socketGet.getSocket().isClosed();
//                boolean connected = socket.isConnected() && ! socket.isClosed();
//                Log.d("stream", "서버 연결 확인 쓰레드 시작2");
                if (result) {
                    Log.d("stream", "server connect complete~~~~~1111111");

                    try {
                        while ((line = socketGet.getBufferedReader().readLine()) != null) {
                            Log.d("line1", line);
                            if (!line.contains("《") || !line.contains("》")) {
                                Log.w("Chatting is error1", "error");
                                continue;
                            }

                            Log.w("Chatting is running", "1");
                            Log.d("ChattingStart", "Start Thread2222");
                            int idx_ment = line.indexOf("《");
                            String real_ment = line.substring(idx_ment + 1);
                            Log.d("made_line", real_ment);
                            int idx = real_ment.indexOf("《");
                            Log.d("chatting Test", String.valueOf(idx));
                            tcp_type = real_ment.substring(0, idx);
                            content = real_ment.substring(idx + 1);
                            Log.d("Chatting is content", content);
                            Log.d("Chatting is tcp_type", tcp_type);

                            //chatting
                            if (tcp_type.equals("1")) {
                                idx = content.indexOf("《");
                                content = content.substring(idx + 1);

                                idx = content.indexOf("》");
                                to = content.substring(0, idx);
                                ment = content.substring(idx + 1);
                                to_array.add(to);
                                ment_array.add(ment);
                                chatting.sendEmptyMessage(0);
                                Log.d("Chatting is to", to);
                                Log.d("Chatting is ment", ment);
                            }

                            //user_list_status
                            else if (tcp_type.equals("2")) {

                                int test_int = 1;
                                ready_list = new ArrayList<>();
                                while (true) {
                                    if (content.contains("《")) {
                                        idx = content.indexOf("《");
                                        ready_list.add(content.substring(0, idx));
                                        Log.d("list" + test_int, content.substring(0, idx));
                                        content = content.substring(idx + 1);
                                    } else {
                                        //                                ready_list.add(content);
                                        Log.d("last" + test_int, content);
                                        break;
                                    }
                                    test_int = test_int + 1;

                                }
                                Log.d("last" + test_int, content);
                                user_list_status.sendEmptyMessage(0);
                            } else if (tcp_type.equals("2.5")) {
                                all_start.sendEmptyMessage(0);
                                exit = true;
                                connect_check_thread = false;
                                break;

                            } else if (tcp_type.equals("6.5")) {
                                master_start.sendEmptyMessage(0);
                                exit = true;
                                connect_check_thread = false;
                                break;
                            } else if (tcp_type.equals("13")) {
                                Log.d("1313", "13");
                                still_connect.sendEmptyMessage(0);
                            }

                            Log.d("Chatting is running", "2");

                            Log.d("ChattingStart", "Start Thread33333");
                        }
                    } catch (SocketException e) {
                        Log.d("xxxxx", "xxx");
                    reconnect.sendEmptyMessage(0);
//                        return;

                    } catch (IOException e) {
                        e.printStackTrace();
//                    Log.d("Chatting is running", "error!!!!");
                    }
                }

//                }

//                item.add(new ChatData("1111111","  " + "asdasdasd" + "  "));
            /*} catch (IOException e) {
                e.printStackTrace();
                Log.d("Chatting is running", "error!!!!");
            }*/
//            item.add(new ChatData("2222222","  " + "asdasdasd" + "  "));
                if (exit == true){
                    break;
                }
            }
        }
    };

    Handler reconnect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String test = new Tcp_connect(RoomActivity.this).execute("8000",room_num + "《" + id + "《" + tagger_or_not).get();
//                        run = true;
//                        checkUpdate.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    };


    Handler still_connect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tcp_chat = new Tcp_chat();
            tcp_chat.execute(id ,"13《" + room_num + "《" + id + "");
        }
    };

    Handler chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));
            Log.d("chatting_test",ment_array.get(flow));
            item.add(new ChatData(to_array.get(flow),"  " + ment_array.get(flow) + "  "));
            flow = flow + 1;
            room_adapter.notifyDataSetChanged();

        }
    };


    Handler all_start = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(RoomActivity.this, "시작합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RoomActivity.this,DrawActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("room_num",room_num);
            intent.putExtra("status","2");
            startActivity(intent);
            finish();

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
            finish();
        }
    };


    Handler user_list_status = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int idx;
            String other_id,other_status;
            ArrayList<String> check_id = new ArrayList<>();
            boolean check_id_boolean = true;
            item_ready.clear();
//            Log.d("check",ready_list.get(0));
            for (int i = 0; i < ready_list.size(); i++) {
//                Log.d("check",ready_list.get(i));
                idx = ready_list.get(i).indexOf("》");
                other_id = ready_list.get(i).substring(0,idx);
                other_status = ready_list.get(i).substring(idx+1);
                check_id.add(other_id);
//                Log.d("status_",other_status);
                if (i > 0){
                    for (int j = 0; j < check_id.size()-1; j++) {
                        if (other_id.equals(check_id.get(j))){

                            Log.d("overlap!!!!!",other_id + " || " + check_id.get(j));
                            check_id_boolean = false;

                        }
                        else
                            Log.d("non_overlap!!!!!",other_id + " || " + check_id.get(j));
                    }
                }
                if (check_id_boolean == true) {
                    item_ready.add(new ReadyData(other_id, other_status));
                }
                check_id_boolean = true;
            }
            readyAdapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "room stop", Toast.LENGTH_SHORT).show();
    }


    void layout(){
        room_adapter = new RoomAdapter(getLayoutInflater(),item,id);
        readyAdapter = new ReadyAdapter(getLayoutInflater(),this,item_ready);
    }
}