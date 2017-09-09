package com.example.kkk.drawword;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity {
    @BindView(R.id.game_button) Button game_btn;
    @BindView(R.id.friend_button) Button friend_btn;
    @BindView(R.id.proto_test) TextView test;
    @BindView(R.id.port_num) EditText port_num;
    @BindView(R.id.ment) EditText ment;
    @BindView(R.id.tcp_test) Button tcp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame, new gamelist_fragment());
        fragmentTransaction.replace(R.id.fragmentfriendorgame, new gamelist_fragment());
        fragmentTransaction.commit();
        friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchfragment(1);
            }
        });

        game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchfragment(2);
            }
        });

        tcp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = new Database(getApplicationContext(),"user_db", null,2);
               /* String port = port_num.getText().toString();
                String send_ment = ment.getText().toString();
                Tcp_test tcp_test = new Tcp_test();
                tcp_test.execute(port,send_ment);
                Toast.makeText(GameActivity.this, "start", Toast.LENGTH_SHORT).show();*/
                IntentClass intentClass = new IntentClass(GameActivity.this);
                Intent intent = new Intent();
                intentClass.UserLogout(database);
            }
        });


    }



    public void switchfragment(int type){
        Fragment fr;
        if (type == 1){
            fr = new friendlist_fragment();
        }
        else {
            fr = new gamelist_fragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.replace(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.commit();
    }



}
