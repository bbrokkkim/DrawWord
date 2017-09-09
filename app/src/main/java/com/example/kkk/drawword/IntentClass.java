package com.example.kkk.drawword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

/**
 * Created by KKK on 2017-09-09.
 */

public class IntentClass {
    Activity context;
    IntentClass(Activity context){
        this.context = context;
    }
    void PushUserInfo(Intent intent,Database database) {
        String iden = database.show_id("select * from user_token",0,1);
        String id = database.show_id("select * from user_token",0,2);
        String token = database.show_id("select * from user_token",0,3);
        intent.putExtra("user_iden", iden);
        intent.putExtra("user_id", id);
        intent.putExtra("user_token", token);
        Toast.makeText(context, iden, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }
    void GetUserInfo(Database database, String iden, String id,String token){
        iden = database.show_id("select * from user_token",0,1);
        id = database.show_id("select * from user_token",0,2);
        token = database.show_id("select * from user_token",0,3);
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }



    void UserLogout(Database database){
        database.delete("delete from user_token");
        Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        context.finish();
    }
    int UserCount(Database database){
        int count = database.count("select * from user_token");
        return  count;
    } 

    Bitmap GetPhoto(Intent intent){
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);


        Bitmap bitmap = null;
        return bitmap;
    }
}
