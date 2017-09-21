package com.example.kkk.drawword;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.game_button) Button game_btn;
    @BindView(R.id.friend_button) Button friend_btn;
    @BindView(R.id.proto_test) TextView test;
    @BindView(R.id.port_num) EditText port_num;
    @BindView(R.id.ment) EditText ment;
    @BindView(R.id.tcp_test) Button tcp_btn;
    String friend_list_json,iden,id,token,uri;
    Database database;
    IntentClass intentClass = new IntentClass(GameActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        ButterKnife.bind(this);
        database = new Database(GameActivity.this,"user_db", null,1);
        iden = intentClass.GetIden(database);
        id = intentClass.GetId(database);
        token = intentClass.GetToken(database);
        uri = intentClass.GetUri(database);
        Log.d("test_iden",iden);
        if (!iden.equals(null)){
            try {
                friend_list_json = new OkhttpFriend(this).execute("1",iden,token).get();
                Log.d("friend_json",friend_list_json);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("friend_json1","test");
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.d("friend_json2","test2");
            }
        }
        Log.d("friend_json","??");
        //프레그먼트 호출
        switchfragment(1);

        //버튼
        friend_btn.setOnClickListener(this);
        game_btn.setOnClickListener(this);
        tcp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intentClass.UserLogout(database);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_button:
                switchfragment(1);
                break;
            case R.id.game_button:
                switchfragment(2);
                break;
        }
    }



    public void switchfragment(int type){
        Fragment fr;
        Bundle bundle = new Bundle();
        if (type == 1){
            fr = new friendlist_fragment();
            bundle.putString("friend_list_json",friend_list_json);
            Log.d("listlistlist", friend_list_json);
        }
        else {
            fr = new gamelist_fragment();
        }
        bundle.putString("iden",iden);
        bundle.putString("id",id);
        bundle.putString("uri", uri);
        Log.d("switch",iden + id + uri);
        fr.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.replace(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.commit();
    }
}
