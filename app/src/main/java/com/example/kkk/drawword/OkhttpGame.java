package com.example.kkk.drawword;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KKK on 2017-09-28.
 */

public class OkhttpGame extends  AsyncTask<Object,Void,String> {
    String output, ch, url;
    String user_iden, token, user_id, friend_id, friend_iden, type;
    RequestBody requestBody;


    @Override
    protected String doInBackground(Object... params) {
        ch = (String) params[0];
        user_iden = (String) params[1];
        user_id = (String) params[2];
        OkHttpClient client = new OkHttpClient();
        //join or login
        if (ch.equals("1")) {
            token = (String) params[2];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("user_iden", user_iden)
                    .addFormDataPart("token", token)
                    .build();
        }
        url = "http://13.124.229.116/php/friend.php";

        Log.d("choice",ch);
        Log.d("user_iden",user_iden);

        Request request = new Request.Builder()
                .url("http://13.124.229.116/php/gamelist.php")
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
}