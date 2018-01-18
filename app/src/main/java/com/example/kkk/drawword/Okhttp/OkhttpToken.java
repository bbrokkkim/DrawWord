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

import static com.example.kkk.drawword.R.id.user_name;

/**
 * Created by KKK on 2017-11-21.
 */

public class OkhttpToken extends AsyncTask<Object,Void,String>{
    String output,ch,url;
    String user_iden,user_name,token;
    RequestBody requestBody;
    String server_ip;

    @Override
    protected String  doInBackground(Object... params) {


        ch = (String) params[0];
        user_iden = (String) params[1];
        user_name = (String) params[2];

        OkHttpClient client = new OkHttpClient();
        //join or login
        if (ch.equals("1")) {
            token = (String) params[3];
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("user_iden", user_iden)
                    .addFormDataPart("user_name", user_name)
                    .addFormDataPart("token", token)
                    .build();
        }
        else if (ch.equals("2")){
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("choice", ch)
                    .addFormDataPart("user_iden", user_iden)
                    .addFormDataPart("user_name", user_name)
                    .build();
        }

        server_ip = MainActivity.server_url;
        url = server_ip + "php/token.php";
        Log.d("choice",ch);
        Log.d("user_iden",user_iden);

        Request request = new Request.Builder()
                .url(url)
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
        Log.d("httpoutput_token",output);

    }


}

