package com.example.kkk.drawword.Okhttp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.RoomActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by KKK on 2017-10-02.
 */

public class TcpChat extends AsyncTask<Object,Void,String> {
    String port_st;
    Socket socket;
    boolean status;
    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;
    String ch;
    Activity activity;
    public TcpChat (Activity activity,Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        this.activity = activity;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.socket = socket;
    }

    @Override
    protected String doInBackground(Object... params) {
        ch = (String) params[0];
        int port = (int) params[1];
        String info = (String) params[2];
        try {
            Log.d("stream","asdd123");
            socket = new Socket("52.78.217.245",port);
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
            ment_writer.println(info);
            Log.d("stream","fin");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fail",e.toString());
        }
        return ch;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(activity, "소켓 연결중..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (status == true) {

            Toast.makeText(activity, "연결됨", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show();
        Log.d("111","1111");

    }


}