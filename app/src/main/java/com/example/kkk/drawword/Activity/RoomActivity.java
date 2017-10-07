package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Data.ChatData;
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
    @BindView(R.id.text_ment) EditText text;
    @BindView(R.id.ment_view) ListView listView;
    @BindView(R.id.ready_list) ListView readylist;
    @BindView(R.id.room_name) TextView roomname;
    private Handler mHandler;

    ArrayList<ChatData> item= new ArrayList();
    RoomAdapter room_adapter;
    EditText port_num;
    Button submit;
    ChatData chatData;
    boolean socket_condition = true;
    String read1 = "";
    String read2 = "";
    String user_name,ment,to;

    Socket socket;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    Tcp_chat tcp_chat;
    Tcp_Connect tcp_connect;

    String ip = "13.124.229.116";
    String id,iden,room_name,room_num;
    int port = 8001;

    Handler handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        layout();
        ButterKnife.bind(this);
        Intent get = getIntent();
        id = get.getStringExtra("id");
        iden = get.getStringExtra("iden");
        room_name = get.getStringExtra("room_name");
        int asdf = get.getIntExtra("room_num",1);
        room_num = String.valueOf(asdf);
        roomname.setText(room_num + ".  " + room_name);
        Log.d("asdfas",id);
        String my_info = room_num + "\n" + room_name + "\n" + iden + "\n" + id;
        listView.setAdapter(room_adapter);
        String test = null;
/*        try {
            test = new TcpChat(this,socket,bufferedReader,bufferedWriter).execute("1",port,my_info).get();
//            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("asd",test);*/

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
                String push_content = room_num + "《" + id + "》" + chat_content;
                new Tcp_chat().execute(id ,push_content);
            }
        });


        handler2 = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String asdf =bundle.getString("msg");
//                item.add(new ChatData("나나난",asdf));
            }
        };
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tcp_chat = new Tcp_chat();
        tcp_chat.execute(id ,"fin!@#!@#");
    }


    private Thread checkUpdate = new Thread() {

        public void run() {

                String line = null;
                Log.w("ChattingStart", "Start Thread");
                boolean test = false;
            try {
                while ((line = bufferedReader.readLine()) !=null) {
                    Log.w("Chatting is running", "1");
                    /*if (test == false){
                        test = true;
                        continue;
                    }*/
                    Log.d("line", line);
                    int idx_ment = line.indexOf("《");
                    String real_ment = line.substring(idx_ment + 1);
                    Log.d("made_line", real_ment);
                    int idx = real_ment.indexOf("》");
                    Log.d("chatting Test", String.valueOf(idx));
                    to = real_ment.substring(0, idx);
                    ment = real_ment.substring(idx + 1);
                    Log.d("Chatting is running111", to);
                    Log.d("Chatting is running222", ment);

                    Log.d("Chatting is running", "2");

                    try {
                        handler.sendEmptyMessage(0);
                        Log.d("Chatting is running", "3");
                    } catch (Exception e) {
                        Log.d("Chatting is running", "fail");
                    }

                }
                Log.d("Chatting is running", "21");

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Chatting is running", "2111");

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean check = true;
            Log.d("test",String.valueOf(item.size()));

            /*if (item.size() != 0 ){
                String before =item.get(item.size()).getUser_name();
                if (ment.equals(before)){
                    check = false;
                }
            }*/


            item.add(new ChatData(to,"  " + ment + "  ",check));
            room_adapter.notifyDataSetChanged();
/*
            if (position > 1) {
                Log.d("before", String.valueOf(item.get(position - 2).getUser_name()));
                Log.d("to", String.valueOf(to));
                if (to.equals(item.get(position - 2).getUser_name())) {
                }
            }
*/
            Log.d("test","chat456");
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
        room_adapter = new RoomAdapter(getLayoutInflater(),item,id);
    }
}
