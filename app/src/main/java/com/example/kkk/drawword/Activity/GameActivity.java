package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Database;
import com.example.kkk.drawword.Friend_Data;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.OkhttpFriend;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.friendlist_fragment;
import com.example.kkk.drawword.gamelist_fragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.game_button) Button game_btn;
    @BindView(R.id.friend_button) Button friend_btn;
    @BindView(R.id.proto_test) TextView ment;
    @BindView(R.id.my_info) Button my_info;
    @BindView(R.id.logout) ImageButton logout;
    String friend_list_json,check_json,iden,id,token,uri,friend_iden,friend_id;
    int json_length = 0;
    int json_row = 1;
    int testint = 0;
    Database database;
    IntentClass intentClass = new IntentClass(GameActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_layout);
        ButterKnife.bind(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "Refreshed token~~: " + refreshedToken);
        Intent intent = getIntent();

        database = new Database(GameActivity.this,"user_db", null,1);
        iden = intentClass.GetIden(database);
        id = intentClass.GetId(database);
        token = intentClass.GetToken(database);
        uri = intentClass.GetUri(database);
        Log.d("test_iden",iden);
        if (!iden.equals(null)){
            try {
                check_json = new OkhttpFriend().execute("4",iden).get();
                Log.d("friend_json","test");
                GetJson(check_json,true);
                try {
                    friend_list_json = new OkhttpFriend().execute("1",iden,token).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("friend_json22",friend_list_json);
                switchfragment(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("friend_json1","test1");
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.d("friend_json2","test2");
            }
        }
        Log.d("friend_json","??");
        //프레그먼트 호출


        //버튼
        friend_btn.setOnClickListener(this);
        game_btn.setOnClickListener(this);
        logout.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_button:
                switchfragment(1);
                ment.setText("친구목록");
                break;
            case R.id.game_button:
                switchfragment(2);
                ment.setText("게임목록");
                break;
            case R.id.logout:
                Intent intent = new Intent(GameActivity.this,MainActivity.class);
                intentClass.UserLogout(database,intent);
                break;
        }
    }

    void GetJson(String json_list, boolean type) {
        Log.d("테스트", "not null");
        if (!json_list.equals("nothing")) {
            try {
                JSONArray json = new JSONArray(json_list);
                Log.d("json_length", String.valueOf(json.length()));
                json_length = json.length();
                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = json.getJSONObject(i);
                    friend_id = jsonObject.getString("my_id");
                    Log.d("ilength",String.valueOf(i));
                    Dialog_view(friend_id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void Dialog_view(final String friend_id){
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle(friend_id+"씨가 친구를 추가 했습니다.");
        dialog.setMessage("친구를 추가하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("5",iden,id,friend_id,"1");
                Log.d("length",String.valueOf(json_length));
                Log.d("row",String.valueOf(json_row));
                if (json_length == json_row){
                    try {
                        friend_list_json = new OkhttpFriend().execute("1",iden,token).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    switchfragment(1);
                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    json_row = 1;
                }
                json_row = json_row + 1;
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("5",iden,id,friend_id,"2");
                Log.d("length",String.valueOf(json_length));
                Log.d("row",String.valueOf(json_row));
                if (json_length == json_row){
                    try {
                        friend_list_json = new OkhttpFriend().execute("1",iden,token).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    switchfragment(1);
                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    json_row = 1;
                }
                json_row = json_row + 1;
            }
        });
        dialog.show();
    }

    public void switchfragment(int type){
        Fragment fr;
        Bundle bundle = new Bundle();
        if (type == 1){
            fr = new friendlist_fragment();
            bundle.putString("friend_list_json",friend_list_json);
            bundle.putString("check_json",check_json);
            Log.d("listlistlist", friend_list_json);
        }
        else {
            /*bundle.putInt("test",testint);
            testint = testint + 1;*/
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
