package com.example.kkk.drawword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by KKK on 2017-09-09.
 */

public class IntentClass {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    Activity context;

    public IntentClass(Activity context) {
        this.context = context;
    }

    public void PushUserInfo(Intent intent) {
/*        String iden = database.show_id("select * from user_token", 0, 1);
        String id = database.show_id("select * from user_token", 0, 2);
        String token = database.show_id("select * from user_token", 0, 3);
        String uri = database.show_id("select * from user_token", 0, 4);

        Toast.makeText(context, iden, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, token, Toast.LENGTH_SHORT).show();*/
        context.startActivity(intent);
    }

    public void InsertUserInfo(Database database,String iden,String user_id,String user_token,String uri,String rotate){
        database.insert("INSERT INTO user_token VALUES(null,'" + iden + "','" + user_id + "','" + user_token + "','"+ uri +"','"+rotate+ "');");
    }
    public String GetIden(Database database) {
        String iden = database.show_id("select * from user_token", 0, 1);
        return iden;
    }

    public String GetId(Database database) {
        String id = database.show_id("select * from user_token", 0, 2);
        return id;
    }
    public String GetToken(Database database){
        String token = database.show_id("select * from user_token", 0, 3);
        return token;
    }
    public String GetUri(Database database){
        String uri = database.show_id("select * from user_token", 0, 4);
        return uri;
    }
    public String Getrotate(Database database){
        String rotate = database.show_id("select * from user_token", 0, 5);
        return rotate;
    }




    public void UserLogout(Database database,Intent intent) {
        database.delete("delete from user_token");
        Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    public int UserCount(Database database) {
        int count = database.count("select * from user_token");
        return count;
    }

    public Intent GetPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    public Boolean PermissionStatus(String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음

            Log.d("as","1");
            return true;
        }else{
            // 권한 있음
            Log.d("as","1");
            return false;
        }

    }

    public void PermissionGet(){
        // Activity에서 실행하는경우
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                Toast.makeText(context, "승낙!!", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(context, "승낙?", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
    }


}