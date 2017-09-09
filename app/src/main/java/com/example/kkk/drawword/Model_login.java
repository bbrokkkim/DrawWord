package com.example.kkk.drawword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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
    String id;
    String pwd;
    String info;
    Boolean check = false;

    Connect_login connect_login1 = new Connect_login();
    Model_login(String info){
        this.id = id;
        this.pwd = pwd;
        this.info = info;
    }




 /*   Boolean Connect_http(String id,String pwd){
        Connect_login connect_login = new Connect_login();
        connect_login.execute(id,pwd);

        int sec = 0;

        return check;
    }*/


    class Connect_login extends AsyncTask<String ,String ,String  >{
        String output="as";
        String str;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(output.equals("correct")){
                check = true;
                info = "correct";
            }
            else if (output.equals("uncorrect")){
                check = false;
                info = "uncorrect";
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String) params[1];
            String pwd = (String) params[2];
            String name = (String) params[3];
            String email = (String) params[4];
            String write_certi = (String) params[5];
            String sex = (String) params[6];
            String ch = (String) params[7];


            try {
                URL url = new URL("http://13.124.229.116/join.php?"
                        + "/create_user_id.php?"
                        + "choice=" + ch
                        + String.format("&id=%s", URLEncoder.encode(id,"UTF-8"))
                        + "&pwd=" + pwd
                        + String.format("&name=%s", URLEncoder.encode(name,"UTF-8"))
                        + "&phone=" + email
                        + "&sex=" + sex);
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
                while ((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                output = buffer.toString();
                return output;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
