package com.example.kkk.drawword;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-17.
 */

public class RoomActivity extends Activity {
    private Handler mHandler;
    ListView listView;
    ArrayList<ChatData> item= new ArrayList();
    Room_Adapter room_adapter;
    EditText text,port_num;
    Button submit,refresh;
    ChatData chatData;
    boolean socket_condition = true;
    String read1 = "";
    String read2 = "";
    String user_name,ment;

    Socket socket;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    //Read_thread read_thread;
    Tcp_chat tcp_chat;
    Tcp_Connect tcp_connect;

    String ip = "13.124.229.116";
    int port = 8000;

    Handler handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);
        layout();
        listView.setAdapter(room_adapter);

        //read_thread = new Read_thread();
        tcp_connect = new Tcp_Connect();
        String num = "8000";
        item.add(new ChatData("user_name","hi~"));
        tcp_connect.execute(num);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("test","thread1");
        checkUpdate.start();
        Log.d("test","thread2");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String port = port_num.getText().toString();
                String chat_content = text.getText().toString();
                Toast.makeText(RoomActivity.this, "누름", Toast.LENGTH_SHORT).show();
                tcp_chat = new Tcp_chat();
                tcp_chat.execute("asdf",chat_content);
                /*ClientThread clientThread1;
                clientThread1 = new ClientThread(client, handler2);
                clientThread1.start();
                */
            }
        });


        handler2 = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String asdf =bundle.getString("msg");
                item.add(new ChatData("나나난",asdf));
            }
        };
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show();
    }


    private Thread checkUpdate = new Thread() {

        public void run() {

                String line = null;
                Log.w("ChattingStart", "Start Thread");
            try {
                while ((line = bufferedReader.readLine()) !=null) {
                    Log.w("Chatting is running", "1");
                    try {
                        ment = line;
                        Log.d("Chatting is running", "2");
                        handler.sendEmptyMessage(0);
                        Log.d("Chatting is running", "3");
                    } catch (Exception e) {
                        Log.d("Chatting is running","fail");
                    }



                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test","chat123");
            if (ment.equals("name")){
                Toast.makeText(RoomActivity.this, "맞음", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(RoomActivity.this, "틀림", Toast.LENGTH_SHORT).show();

            item.add(new ChatData("경관",ment));
            room_adapter.notifyDataSetChanged();

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
            ment_writer.println("asdf");

            Log.d("send",ment);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("stream","asdd");
            Toast.makeText(RoomActivity.this, "전송됨", Toast.LENGTH_SHORT).show();


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
        port_num = (EditText) findViewById(R.id.port);
        submit = (Button) findViewById(R.id.submit_ment);
        refresh = (Button) findViewById(R.id.refresh);
        room_adapter = new Room_Adapter(getLayoutInflater(),item);
    }
}
