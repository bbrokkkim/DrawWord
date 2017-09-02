package com.example.kkk.drawword;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity {
    Button game_btn, friend_btn, tcp_btn;
    TextView test;
    EditText port_num,ment;
    DrawerLayout drawerLayout;
    String html = "";
    String user_name;
    Fragment fr;
    Socket socket;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    String masseage = null;
    String masseage_push = "test_succ";
    Thread checkmessage;
    Handler receiver;
    Thread th;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        layout();
        mHandler = new Handler();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame, new gamelist_fragment());
        fragmentTransaction.replace(R.id.fragmentfriendorgame, new gamelist_fragment());
        fragmentTransaction.commit();
        friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchfragment(1);
            }
        });

        game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchfragment(2);
            }
        });



       /* new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    setSocket("13.124.60.238", 8000);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();*/




        tcp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHandler = new Handler();
//                th.start();

                String port = port_num.getText().toString();
                String send_ment = ment.getText().toString();
                /*checkUpdate.start();
                Log.d("asdf","12341234");
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));*/
                Tcp_test tcp_test = new Tcp_test();
                tcp_test.execute(port,send_ment);
                Toast.makeText(GameActivity.this, "start", Toast.LENGTH_SHORT).show();

            }
        });

       /* checkmessage = new Thread() {
            public void run() {
                try {
                    while (true) {
                        try {
                            socket = new Socket("13.124.60.238", 8000);
                            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            Log.d("socket", "stream");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 입력 스트림에서 메시지를 읽는다
                        masseage = bufferedReader.readLine();
                        // 이벤트 핸들러에 이벤트를 전달
                        receiver.sendEmptyMessage(0);
//                        test.setText(masseage);
                    }
                } catch (Exception e) {
                    Log.d("tag", "Receive error");
                }
            }
        };*/

        // 메시지를 화면에 표시하는 이벤트 핸들러
        /*receiver = new Handler() {
            public void handleMessage(Message msg) {
                test.setText(masseage);
            }
        };*/


    }

    private Thread checkUpdate = new Thread() {

        public void run() {
            try {
                String line;
                Log.w("ChattingStart", "Start Thread");
                while (true) {
                    Log.w("Chatting is running", "chatting is running");
                    line = bufferedReader.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }
            } catch (Exception e) {

            }
        }
    };

    private Runnable showUpdate = new Runnable() {

        public void run() {
            Toast.makeText(GameActivity.this, "Coming word: " + html, Toast.LENGTH_SHORT).show();
        }

    };

    public class Tcp_test extends AsyncTask<String ,String ,String>{
        String port_st,ment;
        @Override
        protected String doInBackground(String... params) {
            Log.d("stream","asdd11123123");
            port_st = params[0];
            int port = Integer.parseInt(port_st);
            ment = params[1];
            try {
                Log.d("stream","asdd123");
                socket = new Socket("13.124.60.238",port);
                Log.d("second",String.valueOf(port));
//                "13.124.60.238",8007
                boolean result = socket.isConnected();
                if(result) Log.d("stream","서버에 연결됨");
                else Log.d("stream","서버에 연결안됨");
                Log.d("stream","asdd2");
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d("stream","asdd3");
                PrintWriter out = new PrintWriter(bufferedWriter, true);
                user_name = "경관";
                out.println(user_name);
                Log.d("first",ment);

                PrintWriter out1 = new PrintWriter(bufferedWriter, true);
                out1.println(ment);
                Log.d("second",String.valueOf(port));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("qweqwe",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("stream","asdd");
            if ("test" != null ) {

            }
        }
    }

    public void switchfragment(int type){
        Fragment fr;
        if (type == 1){
            fr = new friendlist_fragment();
        }
        else {
            fr = new gamelist_fragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.replace(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.commit();
    }



    void layout(){
        game_btn = (Button) findViewById(R.id.game_button);
        friend_btn = (Button) findViewById(R.id.friend_button);
        tcp_btn = (Button) findViewById(R.id.tcp_test);
        test = (TextView) findViewById(R.id.proto_test);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        port_num = (EditText) findViewById(R.id.port_num);
        ment = (EditText) findViewById(R.id.ment);
    }
}
