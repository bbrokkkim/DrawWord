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
    String user_name,ment;

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
        Toast.makeText(this, room_num, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, room_name, Toast.LENGTH_SHORT).show();
        roomname.setText(room_num + "Asdf" + room_name);
        Log.d("asdfas",id);
        String my_info = room_num + "\n" + room_name + "\n" + iden + "\n" + id;
        listView.setAdapter(room_adapter);
        try {
            new TcpChat(this,socket,bufferedReader,bufferedWriter).execute("1",port,my_info).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("test","thread1");
        checkUpdate.start();
        Log.d("test","thread2");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat_content = text.getText().toString();
                String push_content = room_num + "\n" + room_name + "\n" + iden + "\n" + id + "\n" + chat_content;
                Toast.makeText(RoomActivity.this, "누름", Toast.LENGTH_SHORT).show();
                tcp_chat = new Tcp_chat();
                tcp_chat.execute(id ,push_content);
/*                ClientThread clientThread1;
                clientThread1 = new ClientThread(client, handler2);
                clientThread1.start();*/
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

            item.add(new ChatData(id,ment));
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
            ment_writer.println(ment);

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
        submit = (Button) findViewById(R.id.submit_ment);
        room_adapter = new RoomAdapter(getLayoutInflater(),item);
    }
}
