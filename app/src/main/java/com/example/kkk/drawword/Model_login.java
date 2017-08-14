package com.example.kkk.drawword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by KKK on 2017-08-14.
 */

public class Model_login {
/*
    String id;
    String pwd;
    Model_login(String id,String pwd){
        this.id = id;
        this.pwd = pwd;
    }
*/
    Boolean check;
    Boolean Connect_http(String id,String pwd){
        Connect_login connect_login = new Connect_login();
        connect_login.execute(id,pwd);

        int sec = 0;
        while(true){
            if (connect_login.getStatus() == AsyncTask.Status.FINISHED){
                return check;
            }
            else if (sec == 10){
                return check;
            }
            else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sec = sec + 1;
                continue;
            }
        }

    }


    class Connect_login extends AsyncTask<String ,String ,String >{
        String output;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(output.equals("correct")){
                check = true;
            }
            else if (output.equals("uncorrect")){
                check = false;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String pwd = params[1];
            try {
                URL url = new URL("");
                String parameter =
                          String.format("id=%s", URLEncoder.encode(id, "UTF-8"))
                        + String.format("pwd=%s", URLEncoder.encode(pwd, "UTF-8"));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(parameter);
                writer.flush();
                writer.close();

                InputStreamReader inputStream = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(inputStream);
                StringBuffer buffer = new StringBuffer();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
