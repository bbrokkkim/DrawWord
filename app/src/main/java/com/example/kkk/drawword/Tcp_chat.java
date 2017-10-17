package com.example.kkk.drawword;

import android.os.AsyncTask;
import android.util.Log;

import java.io.PrintWriter;

/**
 * Created by KKK on 2017-10-17.
 */

public class Tcp_chat extends AsyncTask<String ,String ,String> {
    String user,ment,ment1;
    SocketGet socketGet = SocketGet.getInstance();
    @Override
    protected String doInBackground(String... params) {
        Log.d("stream","async시작");
        user = params[0];
        ment = params[1];

            /*PrintWriter user_writer = new PrintWriter(bufferedWriter, true);
            user_writer.println(user);*/

        PrintWriter ment_writer = new PrintWriter(socketGet.getBufferedWriter(), true);
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
