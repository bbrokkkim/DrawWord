package com.example.kkk.drawword.Okhttp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.MainActivity;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KKK on 2017-11-22.
 */

public class OkhttpInvate extends AsyncTask<Object,Void,String> {
    String output,ch,url;
    String user_name,room_name,room_num,user_list;
    String server_ip;
    RequestBody requestBody;


    @Override
    protected String  doInBackground(Object... params) {


        ch = (String) params[0];
        user_name = (String) params[1];
        room_num = (String) params[2];
        room_name = (String) params[3];
        user_list = (String) params[4];
        OkHttpClient client = new OkHttpClient();
        //join or login
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", user_name)
                .addFormDataPart("room_num", room_num)
                .addFormDataPart("room_name", room_name)
                .addFormDataPart("user_list", user_list)
                .build();

//        url = "http://13.125.120.82/php/push_noti.php";
        server_ip = MainActivity.server_url;
        url = server_ip + "php/push_noti.php";
        Log.d("choice",ch);
        Log.d("url",url);
        Log.d("user_name", user_name);
        Log.d("room_num", room_num);
        Log.d("room_name", room_name);
        Log.d("user_list", user_list);
        Request request = new Request.Builder()
                .url(MainActivity.server_url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            output = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("httpoutput",output);

    }

}
