package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kkk.drawword.Adapter.DrawAdapter;
import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Tcp_chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-10-13.
 */

public class DrawActivity extends Activity{
    @BindView(R.id.draw_listview) ListView listView;
    @BindView(R.id.game_submit) Button submit;
    @BindView(R.id.answer) EditText answer_ed;
    ArrayList<DrawData> item;
    DrawAdapter drawAdapter;

    //tcp
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    String to,ment,content,tcp_type,id,room_num;

    Socket socket;
    Tcp_chat tcp_chat;
    SocketGet socketGet = SocketGet.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        room_num = intent.getStringExtra("room_num");
        item = new ArrayList<>();
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        drawAdapter = new DrawAdapter(getLayoutInflater(),item);
        listView.setAdapter(drawAdapter);

        socket = socketGet.getSocket();
        bufferedReader = socketGet.getBufferedReader();
        bufferedWriter = socketGet.getBufferedWriter();
        Toast.makeText(this, String.valueOf(socketGet.getA()), Toast.LENGTH_SHORT).show();
        checkUpdate.start();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answer_ed.getText().toString();
//                item.add(new DrawData(answer,"aa"));
                Toast.makeText(DrawActivity.this, answer, Toast.LENGTH_SHORT).show();
                String push_content = "1《" + room_num + "《" + id + "》" + answer;
                new Tcp_chat().execute(id ,push_content);

                drawAdapter.notifyDataSetChanged();
                answer_ed.setText("");
            }
        });
    }


    Handler game_chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));

            item.add(new DrawData(to+ " : " + ment , "aa"));
            drawAdapter.notifyDataSetChanged();

        }
    };

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
                    else if (tcp_type.equals("2")) {
                        game_chatting.sendEmptyMessage(0);
                    }
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

            item.add(new DrawData(to+" : " + ment + "  ",""));
            drawAdapter.notifyDataSetChanged();

        }
    };
}
