package com.example.kkk.drawword.Okhttp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.MainActivity;
import com.example.kkk.drawword.Activity.RoomActivity;
import com.example.kkk.drawword.SocketGet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by KKK on 2017-10-17.
 */

public class Tcp_connect extends AsyncTask<String ,String,String >{
    SocketGet socketGet = SocketGet.getInstance();
    String port_st;
    Socket socket;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    Activity activity;
    String socket_ip = MainActivity.sokcet_url;
    public Tcp_connect(Activity activity){
        this.activity = activity;
    }

    boolean status = true;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//            Toast.makeText(RoomActivity.this, "소켓 연결중..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (status == true) {
//            Toast.makeText(activity, "연결됨", Toast.LENGTH_SHORT).show();
            socketGet.setSocket(socket);
            socketGet.setBufferedReader(bufferedReader);
            socketGet.setBufferedWriter(bufferedWriter);


        }
//            Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        port_st = params[0];
        String ment = params[1];
        int port = Integer.parseInt(port_st);
        try {
            Log.d("stream","asdd123");
            socket = new Socket(socket_ip,port);
            Log.d("second",String.valueOf(port));
//                "13.124.60.238",8007
            /*boolean result = socket.isConnected() && ! socket.isClosed();
            if(result) {
                Log.d("stream","서버에 연결됨");
                status = true;
            }
            else {
                Log.d("stream","서버에 연결안됨");
                status = false;
            }*/
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

            PrintWriter ment_writer = new PrintWriter(bufferedWriter, true);
            ment_writer.println(ment);
            Log.d("stream","fin");

            socketGet.setSocket(socket);
            socketGet.setBufferedReader(bufferedReader);
            socketGet.setBufferedWriter(bufferedWriter);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fail",e.toString());
        }
        return "test";
    }

}