package com.example.kkk.drawword.Okhttp;

import android.os.AsyncTask;
import android.util.Log;
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


    @Override
    protected String  doInBackground(Object... params) {


        ch = (String) params[0];
        user_iden = (String) params[1];
        user_name = (String) params[2];
        token = (String) params[3];
        OkHttpClient client = new OkHttpClient();
        //join or login
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_iden", user_iden)
                .addFormDataPart("user_name", user_name)
                .addFormDataPart("token", token)
                .build();

        url = "http://13.124.229.116/php/token.php";

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

