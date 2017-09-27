package com.example.kkk.drawword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-09-25.
 */

public class Dialog {
    Activity context;
    GameActivity gameActivity;
    Service service;
    public Dialog(Service service){
        this.service = service;
    }




    public void Show(final String name){

        AlertDialog.Builder dialog= new AlertDialog.Builder(service);
        dialog.setTitle(name + "씨가 친구를 추가 했습니다.");
        dialog.setMessage("친구를 추가하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                okhttpFriend.execute();
                Toast.makeText(gameActivity,"네네네네네", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(gameActivity, "아니오", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


}
