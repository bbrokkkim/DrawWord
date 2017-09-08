package com.example.kkk.drawword;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by KKK on 2017-09-08.
 */

public class UserInfoAsync extends AsyncTask<String ,String ,String > {
    String str, output;
    MainActivity context;

    UserInfoAsync(MainActivity context){
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "시작", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("set","9");

        if(output.equals(null)){
            Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
        }
        else if (output.equals("로그인")){
            Toast.makeText(context, "로그인", Toast.LENGTH_SHORT).show();
        }
        else if (output.equals("회원가입")){
            Toast.makeText(context, "회원가입", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "??", Toast.LENGTH_SHORT).show();
        Log.d("set","10");

    }


    @Override
    protected String doInBackground(String... params) {
        Log.d("set","1");
        String ch = (String) params[0];
        String id = null;
        String pwd = null;
        String name = null;
        String email = null;
        String write_certi = null;
        String sex = null;
        String parameter = null;
        URL url = null;
        Log.d("asdf",ch);
        try {
            if (ch.equals("5")){
                Log.d("set","2");
                id = (String) params[1];
                pwd = (String) params[2];
                name = (String) params[3];
                email = (String) params[4];
                write_certi = (String) params[5];
                sex = (String) params[6];
    
                url = new URL("http://13.124.229.116/php"
                        + "/join.php?"
                        + "choice=" + ch
                        + String.format("&id=%s", URLEncoder.encode(id,"UTF-8"))
                        + "&pwd=" + pwd
                        + String.format("&name=%s", URLEncoder.encode(name,"UTF-8"))
                        + "&phone=" + email
                        + "&sex=" + sex);
    
    
            }

            else if (ch.equals("6")){
                Log.d("set","3");
                id = (String) params[1];
                pwd = (String) params[2];
                url = new URL("http://13.124.229.116/php"
                        + "/join.php?choice=6");
                
                parameter =
                        String.format("id=%s", URLEncoder.encode(id, "UTF-8"))
                                + String.format("pwd=%s", URLEncoder.encode(pwd, "UTF-8"));
    
    
            }

            Log.d("asd", url.toString());
            Log.d("set","4");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            
            if (ch.equals("6")){
                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(parameter);
                writer.flush();
                writer.close();

            }
            Log.d("set","5");
            InputStreamReader inputStream = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(inputStream);
            StringBuffer buffer = new StringBuffer();
            while ((str = reader.readLine()) != null){
                buffer.append(str);

                Log.d("output", str);
            }
            output = buffer.toString();
            return output;


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("set","16");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("set","7");
        }

        return null;

    }


}
