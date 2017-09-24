package com.example.kkk.drawword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by KKK on 2017-09-08.
 */

public class UserInfoAsync extends AsyncTask<Object ,String ,String > {
    String str, output;
    MainActivity context;
    String ch;
    Database database;

    String attachmentName = "bitmap";
    String attachmentFileName = "bitmap.bmp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    UserInfoAsync(MainActivity context, Database database){
        this.context = context;
        this.database = database;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "시작", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("set", "fin");
        if (ch.equals("3")){
            Toast.makeText(context, output, Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                    JSONArray json = new JSONArray(output);
                    JSONObject json_ob = json.getJSONObject(0);
                    String iden = json_ob.getString("iden");
                    String user_id = json_ob.getString("id");
                    String user_token = json_ob.getString("token");
                    Intent intent = new Intent(context, GameActivity.class);
                    //회원가입 했을 시
                    if (ch.equals("1")) {
                        Toast.makeText(context, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    }
                    //GameActivity로
                    if (iden.equals("wrong")) {
                        Toast.makeText(context, "틀림", Toast.LENGTH_SHORT).show();
                    } else if (!iden.equals("wrong")) {
                        database.insert("INSERT INTO user_token VALUES(null,'" + iden + "','" + user_id + "','" + user_token + "');");
//                Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();
                        IntentClass intentClass = new IntentClass(context);
                        intentClass.PushUserInfo(intent);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected String doInBackground(Object... params) {
        Log.d("set","1");
        ch = (String) params[0];
        String id = null;
        String pwd = null;
        String name = null;
        String phone = null;
        String write_certi = null;
        String sex = null;
        String parameter = null;
        URL url = null;
        Log.d("asdf",ch);
        try {
            if (ch.equals("1")){
                Log.d("set","2");
                id = (String) params[1];
                pwd = (String) params[2];
                name = (String) params[3];
                phone = (String) params[4];
                write_certi = (String) params[5];
                sex = (String) params[6];
    
                url = new URL("http://13.124.229.116/php"
                        + "/join.php");

                parameter = String.format("id=%s", URLEncoder.encode(id, "UTF-8"))
                        + String.format("&pwd=%s", URLEncoder.encode(pwd, "UTF-8"))
                        + String.format("&name=%s", URLEncoder.encode(name,"UTF-8"))
                        + "&phone=" + phone
                        + "&sex=" + sex
                        + String.format("&choice=%s", URLEncoder.encode(ch,"UTF-8"));
            }

            else if (ch.equals("2")){
                Log.d("set","3");
                id = (String) params[1];
                pwd = (String) params[2];
                url = new URL("http://13.124.229.116/php"
                        + "/login.php");
                
                parameter =
                        String.format("id=%s", URLEncoder.encode(id, "UTF-8"))
                                + String.format("&pwd=%s", URLEncoder.encode(pwd, "UTF-8"));
                Log.d("a",parameter);
            }

            if (ch.equals("3")){
                url = new URL("http://13.124.229.116/php"
                        + "/join.php?choice=3");

            }

            Log.d("URLURLURL", url.toString());
//            Log.d("set",parameter);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            if(ch.equals("3")){
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
                Bitmap bitmap = (Bitmap) params[1];
                Uri uri = (Uri) params[2];
//                conn.setRequestProperty("uploaded_file", String.valueOf(bitmap));

                // content wrapper시작
                DataOutputStream request = new DataOutputStream(
                        conn.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);

// Bitmap을 ByteBuffer로 전환

                byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
                for (int i = 0; i < bitmap.getWidth(); ++i) {
                    for (int j = 0; j < bitmap.getHeight(); ++j) {
                        pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                    }
                }

                /*ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
                bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
                byte[] byteArray = stream.toByteArray() ;
*/
//                request.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "test123.jpg"+"\"" + crlf);
//                request.write(pixels);/*
//                request.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"image.jpg\"\r\n");
                request.writeBytes("\r\n--" + boundary + "\r\n");

                /*File f = new File(uri.getPath());
                FileInputStream fileInputStream = new FileInputStream(uri.toString());
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0)
                {
                // Upload file part(s)
                    DataOutputStream dataWrite = new DataOutputStream(conn.getOutputStream());
                    dataWrite.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                fileInputStream.close();*/;
                request.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"uploadedfile\"\r\n");


                request.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                request.writeBytes("\r\n--" + boundary + "--\r\n");
                request.write(pixels);
                request.flush();
//                request.writeUTF(bitmap.toString());

                // content wrapper종료
                /*request.writeBytes(this.crlf);
                request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
// buffer flush
                request.flush();
                request.close();
*/
                parameter = "choice=3";
            }

            /*OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            writer.write(parameter);
            writer.flush();
            writer.close();
*/
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
