package com.example.kkk.drawword.Okhttp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.kkk.drawword.Activity.MainActivity;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KKK on 2017-09-28.
 */

public class OkhttpGame extends AsyncTask<Object,Void,String> {
    String output, ch, url;
    String user_iden, token, user_id, friend_id, friend_iden, type,room_name,limit;
    String server_ip;
    int room_num;
    RequestBody requestBody;

    @Override
    protected String doInBackground(Object... params) {
        ch = (String) params[0];

        OkHttpClient client = new OkHttpClient();
        //join or login

        if (ch.equals("0")) {
            room_name = (String) params[1];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("room_name", room_name)
                    .build();

            Log.d("room_name",room_name);
        }
        else if (ch.equals("1")) {
            room_name = (String) params[1];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("room_name", room_name)
                    .build();

            Log.d("room_name",room_name);
        }
        else if (ch.equals("2")) {
            limit = (String) params[1];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("limit",limit)
                    .build();
        }

        else if (ch.equals("3")){
            room_num = (int) params[1];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("room_num", String.valueOf(room_num))
                    .build();
        }
        else if (ch.equals("4")) {
            limit = (String) params[1];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("limit",limit)
                    .build();
        }

//        url = "http://13.125.120.82/php/gamelist.php";
        server_ip = MainActivity.server_url;
        url = server_ip + "php/gamelist.php";
        Log.d("choice!!!!",ch);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            output = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("choice!!!!","false");
        }

        return output;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Log.d("outputhttp",output);
    }


}