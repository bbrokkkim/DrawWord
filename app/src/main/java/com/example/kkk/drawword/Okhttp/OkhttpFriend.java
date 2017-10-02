package com.example.kkk.drawword.Okhttp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KKK on 2017-09-19.
 */

public class OkhttpFriend extends AsyncTask<Object,Void,String>{
    String output,ch,url;
    String user_iden,token,user_id,friend_id,friend_iden,type;
    RequestBody requestBody;

    public OkhttpFriend( ){}

    @Override
    protected String  doInBackground(Object... params) {


        ch = (String) params[0];
        user_iden = (String) params[1];

        OkHttpClient client = new OkHttpClient();
        //join or login
        if (ch.equals("1")) {
            token = (String) params[2];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("user_iden", user_iden)
                    .addFormDataPart("token", token)
                    .build();

        } // insert
        else if (ch.equals("2")){
            user_id = (String) params[2];
            friend_id = (String) params[3];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice",ch)
                    .addFormDataPart("user_iden",user_iden)
                    .addFormDataPart("user_id",user_id)
                    .addFormDataPart("friend_id",friend_id)
                    .build();
        } // add friend
        else if (ch.equals("3")){
            friend_iden = (String) params[2];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice",ch)
                    .addFormDataPart("user_iden",user_iden)
                    .addFormDataPart("friend_iden",friend_iden)
                    .build();
        }
        else if (ch.equals("4")){
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice",ch)
                    .addFormDataPart("user_iden",user_iden)
                    .build();
        } // accept
        else if (ch.equals("5")) {
            user_id = (String) params[2];
            friend_id = (String) params[3];
            type = (String) params[4];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("type", type)
                    .addFormDataPart("user_iden", user_iden)
                    .addFormDataPart("user_id", user_id)
                    .addFormDataPart("friend_id", friend_id)
                    .build();
        }
        url = "http://13.124.229.116/php/friend.php";

        Log.d("choice",ch);
        Log.d("user_iden",user_iden);

        Request request = new Request.Builder()
                .url("http://13.124.229.116/php/friend.php")
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
