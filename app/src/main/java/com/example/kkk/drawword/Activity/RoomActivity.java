package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Adapter.ReadyAdapter;
import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.Data.ReadyData;
import com.example.kkk.drawword.Okhttp.TcpChat;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.Adapter.RoomAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */
public class RoomActivity extends Activity {
    @BindView(R.id.fl_activity_main_container) FrameLayout frameLayout;
    @BindView(R.id.text_ment) EditText text;
    @BindView(R.id.ment_view) ListView listView;
    @BindView(R.id.ready_list) ListView readylist;
    @BindView(R.id.room_name) TextView roomname;
    @BindView(R.id.test) Button test_btn;
    @BindView(R.id.ready_btn) Button ready_btn;
    @BindView(R.id.my_ready) TextView my_ready;
    private Handler mHandler;

    private String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod"};


    ArrayList<ChatData> item= new ArrayList();
    ArrayList<ReadyData> item_ready = new ArrayList<>();
    ArrayList<String> ready_list;
    RoomAdapter room_adapter;
    ReadyAdapter readyAdapter;
    EditText port_num;
    Button submit;
    ChatData chatData;
    boolean socket_condition = true;
    String read1 = "";
    String read2 = "";
    String user_name,ment,to;
    String content;
    Socket socket;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    Tcp_chat tcp_chat;
    Tcp_Connect tcp_connect;
    boolean ready = true;
    String ip = "13.124.229.116";
    String id,iden,room_name,room_num;


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

        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(room_adapter);
        readylist.setAdapter(readyAdapter);
        readylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ida) {

                Toast.makeText(RoomActivity.this, "test", Toast.LENGTH_SHORT).show();
                String push_content = "2《" + room_num + "《" + id + "》";
                new Tcp_chat().execute(id ,push_content);
            }
        });


        my_ready.setText(id);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ready_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ready_content;
                if (ready == true){
                    ready_content = "wait";
                    ready_btn.setText("right");
                    ready = false;
                }
                else {
                    ready_content = "ready";
                    ready_btn.setText("wait");
                    ready = true;
                }
                Toast.makeText(RoomActivity.this, ready_content, Toast.LENGTH_SHORT).show();
                Toast.makeText(RoomActivity.this, id, Toast.LENGTH_SHORT).show();
                String push_content = "2《" + room_num + "《" + id + "》" + ready_content;
                new Tcp_chat().execute(id ,push_content);
            }
        });
        String test = null;

        try {
            test = new Tcp_Connect().execute("8000",room_num + "《" + id).get();
            Log.d("test","thread1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        checkUpdate.start();

        Log.d("test","thread2");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat_content = text.getText().toString();
                String push_content = "1《" + room_num + "《" + id + "》" + chat_content;
                new Tcp_chat().execute(id ,push_content);
            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tcp_chat = new Tcp_chat();
        tcp_chat.execute(id ,"fin!@#!@#》");
    }


    private Thread checkUpdate = new Thread() {

        public void run() {
            String line = null;
            Log.w("ChattingStart", "Start Thread");
            String tcp_type = null;
            content = null;
            boolean test = false;
            try {
                while ((line = bufferedReader.readLine()) !=null) {
                    Log.w("Chatting is running" , "1");

                    Log.d("line", line);
                    int idx_ment = line.indexOf("《");
                    String real_ment = line.substring(idx_ment + 1);
                    Log.d("made_line", real_ment);
                    int idx = real_ment.indexOf("《");
                    Log.d("chatting Test", String.valueOf(idx));
                    tcp_type = real_ment.substring(0, idx);
                    content = real_ment.substring(idx+1);
                    Log.d("Chatting is content", content);
                    Log.d("Chatting is tcp_type", tcp_type);


                    //chatting
                    if (tcp_type.equals("1")){
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
                                ready_list.add(content);
                                Log.d("last"+test_int,content);
                                break;
                            }
                            test_int = test_int + 1;
                        }
                        user_list_status.sendEmptyMessage(0);
                    }
                    else if (tcp_type.equals("5")){
                        all_start.sendEmptyMessage(0);
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
            for (int i = 0; i < ready_list.size(); i++) {
                idx = ready_list.get(i).indexOf("》");
                other_id = ready_list.get(i).substring(0,idx);
                other_status = ready_list.get(i).substring(idx+1);
                Log.d("status",other_status);
                item_ready.add(new ReadyData(other_id,other_status));
            }
            readyAdapter.notifyDataSetChanged();

        }
    };



    public class Tcp_chat extends AsyncTask<String ,String ,String> {
        String user,ment,ment1;
        @Override
        protected String doInBackground(String... params) {
            Log.d("stream","async시작");
            user = params[0];
            ment = params[1];

            /*PrintWriter user_writer = new PrintWriter(bufferedWriter, true);
            user_writer.println(user);*/

            PrintWriter ment_writer = new PrintWriter(bufferedWriter, true);
            ment_writer.println(ment);

            Log.d("send",ment);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("stream","asdd");


        }
    }



    public class Tcp_Connect extends AsyncTask<String ,String,String >{
        String port_st;
        boolean status;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(RoomActivity.this, "소켓 연결중..", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            
            if (status == true) {

                Toast.makeText(RoomActivity.this, "연결됨", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(RoomActivity.this, "실패", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            port_st = params[0];
            String ment = params[1];
            int port = Integer.parseInt(port_st);
            try {
                Log.d("stream","asdd123");
                socket = new Socket("13.124.229.116",port);
                Log.d("second",String.valueOf(port));
//                "13.124.60.238",8007
                boolean result = socket.isConnected();
                if(result) {
                    Log.d("stream","서버에 연결됨");
                    status = true;
                }
                else {
                    Log.d("stream","서버에 연결안됨");
                    status = false;
                }
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                PrintWriter ment_writer = new PrintWriter(bufferedWriter, true);
                ment_writer.println(ment);
                Log.d("stream","fin");

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("fail",e.toString());
            }
            return null;
        }
    }

    void layout(){
        listView = (ListView) findViewById(R.id.ment_view);
        text = (EditText) findViewById(R.id.text_ment);
        submit = (Button) findViewById(R.id.submit_ment);
        room_adapter = new RoomAdapter(getLayoutInflater(),item);
        readyAdapter = new ReadyAdapter(getLayoutInflater(),this,item_ready);
    }
}
