package com.example.kkk.drawword.Okhttp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.MainActivity;
import com.example.kkk.drawword.Database;
import com.example.kkk.drawword.IntentClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    String file_uri = null;
    String exifDegree_String = "0";
    int exifDegree;
    String ch;
    String server_ip;
    String rotate;
    Response response;
    Activity activity;
    Database database;
    String iden,user_id,user_pwd,user_token,uri;
    IntentClass intentClass = new IntentClass(activity);
    File file;
    Bitmap image_bitmap;
    public OkhttpUser(Activity activity, Database database){
        this.activity = activity;
        this.database = database;
    }
    @Override
    protected Void doInBackground(Object... params) {
        ch = (String) params[0];
        id = (String) params[1];
        pwd = (String) params[2];
        server_ip = MainActivity.server_url;
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
                exifDegree_String = (String) params[8];
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
                            .addFormDataPart("rotate", exifDegree_String)
                            .build();
                } else {
                    /*file = new File(photo_uri+ "1");*/

//      Uri = url = new Uri();
                    image_bitmap= (Bitmap) params[9];
                    ExifInterface exif = new ExifInterface(photo_uri);

                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegrees(orientation);
                    int width = image_bitmap.getWidth();
                    int height = image_bitmap.getHeight();

                    if (exifDegree != 0){
                        Matrix matrix = new Matrix();
                        matrix.postRotate(exifDegree);
                        image_bitmap = Bitmap.createBitmap(image_bitmap, 0, 0, width, height, matrix, true);
//                        Toast.makeText(activity, ""+ exifDegree + "1", Toast.LENGTH_SHORT).show();
                        Log.d("a","1");
                    }
                    else {
                        Log.d("a","2");
//                        Toast.makeText(getActivity(), ""+ exifDegree+ "2", Toast.LENGTH_SHORT).show();
                    }
                    file_uri = saveBitmapToJpeg(activity,image_bitmap, id + ".jpg");
                    Log.d("file_uri", file_uri);
//                    matrix.postRotate(-90);

//                    Bitmap resizedBitmap = Bitmap.createBitmap(image_bitmap, 0, 0, width, height, matrix, true);



                    Log.d("type", "2");
                    requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("choice", String.format("%s", URLEncoder.encode(ch, "UTF-8")))
                            .addFormDataPart("id", String.format("%s", URLEncoder.encode(id, "UTF-8")))
                            .addFormDataPart("pwd", String.format("%s", URLEncoder.encode(pwd, "UTF-8")))
                            .addFormDataPart("name", String.format("%s", URLEncoder.encode(name, "UTF-8")))
                            .addFormDataPart("phone", String.format("%s", URLEncoder.encode(phone, "UTF-8")))
                            .addFormDataPart("sex", String.format("%s", URLEncoder.encode(sex, "UTF-8")))
                            .addFormDataPart("photo_uri", photo_uri)
                            .addFormDataPart("uploadedfile", id + ".jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(file_uri)))
                            .addFormDataPart("rotate", exifDegree_String)
                            .build();
                }
                Log.d("output", "2");

                url = server_ip + "php/join.php";
//                url = "http://13.125.120.82/php/join.php";
            }

            else if(ch.equals("2")){
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id",String.format("%s", URLEncoder.encode(id, "UTF-8")))
                        .addFormDataPart("pwd",String.format("%s", URLEncoder.encode(pwd, "UTF-8")))
                        .build();
                url = server_ip + "php/login.php";
//                url = "http://13.125.120.82/php/login.php";
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
        //중복검사
        if (output.equals("overlap")){
            Log.d("testttt","loverlap");
            Toast.makeText(activity, "아이디가 중복입니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            GetJson();
            if (iden.equals("wrong")) {
                Toast.makeText(activity, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            } else {
                intentClass.InsertUserInfo(database, iden, user_id, user_token, uri,rotate);
                Intent intent = new Intent(activity, GameActivity.class);
                intent.putExtra("first_login",true);

                activity.startActivity(intent);
                activity.finish();
            }
        }

    }


    public static String saveBitmapToJpeg(Context context, Bitmap bitmap, String name){

        File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로

        String fileName = name + ".jpg";  // 파일이름은 마음대로!

        File tempFile = new File(storage,fileName);

        try{
            tempFile.createNewFile();  // 파일을 생성해주고

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌

            out.close(); // 마무리로 닫아줍니다.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴해주면 끝!
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } return 0;
    }

    void GetJson(){
        try {
            JSONArray json = new JSONArray(output);
            JSONObject json_ob = json.getJSONObject(0);
            iden = json_ob.getString("iden");
            user_id = json_ob.getString("id");
            user_token = json_ob.getString("token");
            uri = json_ob.getString("photo_uri");
            rotate = json_ob.getString("rotate");
//            Intent intent = new Intent(activity, GameActivity.class);
            //회원가입 했을 시
            if (ch.equals("1")) {
                Toast.makeText(activity, "회원가입 완료", Toast.LENGTH_SHORT).show();
            }
            //GameActivity로
            if (iden.equals("wrong")) {
                Toast.makeText(activity, "틀림", Toast.LENGTH_SHORT).show();
            }
            else if (!iden.equals("wrong")) {
                intentClass.InsertUserInfo(database,iden,user_id,user_token,uri,rotate);
                database.insert("INSERT INTO user_token VALUES(null,'"
                        + iden + "','" + user_id + "','" + user_token + "','"+ uri +"','"+ rotate +"');");
//                Toast.makeText(activity, "INSERT INTO user_token VALUES(null,'"
//                        + iden + "','" + user_id + "','" + user_token + "','"+ uri +"','"+ rotate +"');", Toast.LENGTH_SHORT).show();
                Log.d("query" , "INSERT INTO user_token VALUES(null,'"
                        + iden + "','" + user_id + "','" + user_token + "','"+ uri +"','"+ rotate +"');");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
