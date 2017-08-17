package com.example.kkk.drawword;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity {
    Button game_btn,friend_btn;
    Fragment fr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        layout();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentfriendorgame,new friendlist_fragment());
        fragmentTransaction.replace(R.id.fragmentfriendorgame,new friendlist_fragment());
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

    void layout(){
        game_btn = (Button) findViewById(R.id.game_button);
        friend_btn = (Button) findViewById(R.id.friend_button);
    }
}
