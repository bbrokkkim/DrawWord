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

    IntentClass(Activity context) {
        this.context = context;
    }

    void PushUserInfo(Intent intent, Database database) {
        String iden = database.show_id("select * from user_token", 0, 1);
        String id = database.show_id("select * from user_token", 0, 2);
        String token = database.show_id("select * from user_token", 0, 3);
        intent.putExtra("user_iden", iden);
        intent.putExtra("user_id", id);
        intent.putExtra("user_token", token);
        Toast.makeText(context, iden, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    void GetUserInfo(Database database, String iden, String id, String token) {
        iden = database.show_id("select * from user_token", 0, 1);
        id = database.show_id("select * from user_token", 0, 2);
        token = database.show_id("select * from user_token", 0, 3);
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }


    void UserLogout(Database database) {
        database.delete("delete from user_token");
        Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        context.finish();
    }

    int UserCount(Database database) {
        int count = database.count("select * from user_token");
        return count;
    }

    Bitmap GetPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        context.startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        context.startActivity(intent);
        Bitmap bitmap = null;

        if(PermissionStatus(Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionGet();
        }
        else {
            PermissionGet();
            Toast.makeText(context, "권한 없음", Toast.LENGTH_SHORT).show();
        }



/*
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), intent.getData());
        } catch (IOException e) {
            Toast.makeText(context, "asdf", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
*/
        return bitmap;
    }

    Boolean PermissionStatus(String permission){
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

    void PermissionGet(){
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