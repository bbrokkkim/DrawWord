package com.example.kkk.drawword;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KKK on 2017-09-17.
 */

public class OkhttpUser extends AsyncTask<Object,Void,Void>{
    String output,id,pwd,name,phone,sex,write_certi,photo_uri;
    String ch;
    Response response;
    Activity activity;
    Database database;
    String iden,user_id,user_pwd,user_token,uri;
    IntentClass intentClass = new IntentClass(activity);
    public OkhttpUser(Activity activity, Database database){
        this.activity = activity;
        this.database = database;
    }
    @Override
    protected Void doInBackground(Object... params) {
        ch = (String) params[0];
        id = (String) params[1];
        pwd = (String) params[2];

        String url = null;
        RequestBody requestBody = null;
        Log.d("output","0");


        Log.d("output","3");
        OkHttpClient client1 = new OkHttpClient();

        //post 인자
        try {
            Log.d("output","1");

            if(ch.equals("1")) {
                name = (String) params[3];
                phone = (String) params[4];
                write_certi = (String) params[5];
                sex = (String) params[6];
                photo_uri = (String) params[7];
                Log.d("uri", photo_uri);
                if (photo_uri.equals("null")) {
                    Log.d("type", "1");
                    requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("choice", String.format("%s", URLEncoder.encode(ch, "UTF-8")))
                            .addFormDataPart("id", String.format("%s", URLEncoder.encode(id, "UTF-8")))
                            .addFormDataPart("pwd", String.format("%s", URLEncoder.encode(pwd, "UTF-8")))
                            .addFormDataPart("name", String.format("%s", URLEncoder.encode(name, "UTF-8")))
                            .addFormDataPart("phone", String.format("%s", URLEncoder.encode(phone, "UTF-8")))
                            .addFormDataPart("sex", String.format("%s", URLEncoder.encode(sex, "UTF-8")))
                            .addFormDataPart("photo_uri", String.format("%s", URLEncoder.encode(photo_uri, "UTF-8")))
                            .build();
                } else {
                    Log.d("type", "2");
                    requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("choice", String.format("%s", URLEncoder.encode(ch, "UTF-8")))
                            .addFormDataPart("id", String.format("%s", URLEncoder.encode(id, "UTF-8")))
                            .addFormDataPart("pwd", String.format("%s", URLEncoder.encode(pwd, "UTF-8")))
                            .addFormDataPart("name", String.format("%s", URLEncoder.encode(name, "UTF-8")))
                            .addFormDataPart("phone", String.format("%s", URLEncoder.encode(phone, "UTF-8")))
                            .addFormDataPart("sex", String.format("%s", URLEncoder.encode(sex, "UTF-8")))
                            .addFormDataPart("photo_uri", photo_uri)
                            .addFormDataPart("uploadedfile", id + ".jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(photo_uri)))
                            .build();
                }
                Log.d("output", "2");
                url = "http://13.124.229.116/php/join.php";
            }

            else if(ch.equals("2")){
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id",String.format("%s", URLEncoder.encode(id, "UTF-8")))
                        .addFormDataPart("pwd",String.format("%s", URLEncoder.encode(pwd, "UTF-8")))
                        .build();
                url = "http://13.124.229.116/php/login.php";
            }
            Request request1 = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Log.d("output","4");
            response = client1.newCall(request1).execute();

            Log.d("output","5");
            output = response.body().string();
            Log.d("output","6");
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.d("output","f1");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("output","f2");

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.d("httpoutput",output);
        GetJson();
        if (iden.equals("wrong")){
            Toast.makeText(activity, "틀림", Toast.LENGTH_SHORT).show();
        }
        else{
            intentClass.InsertUserInfo(database,iden,user_id,user_token,uri);
            Intent intent = new Intent(activity , GameActivity.class);
            activity.startActivity(intent);
        }

    }

    void GetJson(){
        try {
            JSONArray json = new JSONArray(output);
            JSONObject json_ob = json.getJSONObject(0);
            iden = json_ob.getString("iden");
            user_id = json_ob.getString("id");
            user_token = json_ob.getString("token");
            uri = json_ob.getString("photo_uri");
            Intent intent = new Intent(activity, GameActivity.class);
            //회원가입 했을 시
            if (ch.equals("1")) {
                Toast.makeText(activity, "회원가입 완료", Toast.LENGTH_SHORT).show();
            }
            //GameActivity로
            if (iden.equals("wrong")) {
                Toast.makeText(activity, "틀림", Toast.LENGTH_SHORT).show();
            } else if (!iden.equals("wrong")) {
                intentClass.InsertUserInfo(database,iden,user_id,user_token,uri);
                database.insert("INSERT INTO user_token VALUES(null,'"
                        + iden + "','" + user_id + "','" + user_token + "','"+ uri + "');");
//                Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
