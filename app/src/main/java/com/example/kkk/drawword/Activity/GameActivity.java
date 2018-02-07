package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Database;
import com.example.kkk.drawword.Dialog;
import com.example.kkk.drawword.Fragment.GamelistFragment;
import com.example.kkk.drawword.GameListGet;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.InvateList;
import com.example.kkk.drawword.Okhttp.OkhttpFriend;
import com.example.kkk.drawword.Okhttp.OkhttpGame;
import com.example.kkk.drawword.Okhttp.OkhttpToken;
import com.example.kkk.drawword.Okhttp.OkhttpUser;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.Fragment.FriendlistFragment;
//import com.example.kkk.drawword.Test2Activity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by KKK on 2017-08-16.
 */

public class GameActivity extends Activity implements View.OnClickListener{
    private UncaughtExceptionHandler mUncaughtExceptionHandler;
    @BindView(R.id.game_button) Button game_btn;
    @BindView(R.id.friend_button) Button friend_btn;
    @BindView(R.id.proto_test) TextView ment;
    static ImageButton invate;
    static ImageView invate_pocket;
    static TextView invate_ment;
    static FrameLayout invate_pocket_group;
    @BindView(R.id.my_info) Button my_info;
    @BindView(R.id.back_btn) ImageButton logout;
    public static String friend_list_json;
    public String game_list_json = "";
    public int game_list_focus = 0;
    static ArrayList<InvateList> invateArrayList = new ArrayList<>();
    static InvateList invateList;
    public static String user_iden_static = "";
    public static String user_name_static = "";
    static String friend_name;
    static String friend_room_num;
    static String friend_room_name;
    String check_json;
    String iden;
    String id;
    String token;
    boolean fcm_token;
    String uri;
    String friend_iden;
    String friend_id;
    int json_length = 0;
    int json_row = 1;
    int testint = 0;
    Fragment fr;
    FragmentManager fm;
    Database database;
    IntentClass intentClass = new IntentClass(GameActivity.this);
    GameListGet gameListGet = GameListGet.getInstance();



    public static void modify(String user_name, String room_num, String room_name){
        invate.setVisibility(View.VISIBLE);
        invate_pocket_group.setVisibility(View.VISIBLE);
        invate_pocket_group.bringToFront();
        invateList = new InvateList(user_name,room_num);
        invateArrayList.add(invateList);
        friend_name = user_name;
        friend_room_num = room_num;
        friend_room_name = room_name;
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        new Invate_time().start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.game_layout);
//        Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();
        invate = (ImageButton) findViewById(R.id.invate_btn);
        invate_pocket = (ImageView) findViewById(R.id.invate_pocket);
        invate_ment = (TextView) findViewById(R.id.invate_ment);
        invate_pocket_group = (FrameLayout) findViewById(R.id.invate_pocket_group);

        ButterKnife.bind(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "Refreshed token~~: " + refreshedToken);
        Intent intent = getIntent();
        invate_pocket_group.setVisibility(View.GONE);
        database = new Database(GameActivity.this,"user_db", null,1);
        iden = intentClass.GetIden(database);
        id = intentClass.GetId(database);
        token = intentClass.GetToken(database);
        uri = intentClass.GetUri(database);
        user_iden_static = iden;
        user_name_static = id;
//        Toast.makeText(this, user_iden_static, Toast.LENGTH_SHORT).show();


        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerApplication());
//        Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();

        Log.d("test_iden",iden);
        if (!iden.equals(null)){
            try {
                game_list_json = new OkhttpGame().execute("2","0").get();
                gameListGet.addGameList(game_list_json);
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
        
        fcm_token = intent.getBooleanExtra("first_login",false);
        if (fcm_token == true){
            new OkhttpToken().execute("1",iden,id,refreshedToken);
//            Toast.makeText(this, "로그인 됨!!!!", Toast.LENGTH_SHORT).show();
//            new OkhttpToken().execute("choice", iden,FirebaseInstanceId.getInstance().getToken());

        }/*
        else
            Toast.makeText(this, "아님", Toast.LENGTH_SHORT).show();
*/
        //버튼
        friend_btn.setOnClickListener(this);
        game_btn.setOnClickListener(this);
        logout.setOnClickListener(this);
        my_info.setOnClickListener(this);
        invate.setOnClickListener(this);
    }
    class UncaughtExceptionHandlerApplication implements Thread.UncaughtExceptionHandler{

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            //예외상황이 발행 되는 경우 작업
//            Log.e("Error", getStackTrace(ex));
            Toast.makeText(GameActivity.this, "에러", Toast.LENGTH_SHORT).show();
            //예외처리를 하지 않고 DefaultUncaughtException으로 넘긴다.
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_button:
                switchfragment(1);
                ment.setText("친구목록");
//                Toast.makeText(this, game_list_json, Toast.LENGTH_SHORT).show();
                Log.d("add json",game_list_json);
                break;
            case R.id.game_button:
                switchfragment(2);
                ment.setText("게임목록");

                break;
            case R.id.back_btn:
                super.onBackPressed();
                break;
            case R.id.my_info:
//                Toast.makeText(this, "구현 안해", Toast.LENGTH_SHORT).show();
                Dialog_logout();
                break;
            case R.id.invate_btn :
//                Toast.makeText(this, "초대", Toast.LENGTH_SHORT).show();
                Dialog_invate(friend_name,friend_room_num,friend_room_name);
                invate.setVisibility(View.GONE);
                break;
        }
    }
    static class Invate_time extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invate_view.sendEmptyMessage(0);
        }
    }
    static Handler invate_view = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            invate_pocket.setVisibility(View.GONE);
            invate_pocket_group.setVisibility(View.GONE);
        }
    };
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
    void Dialog_invate(String user_name, final String room_num, final String room_name){
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("초대메세지");
        dialog.setMessage(room_num + "번 방에서 "+ user_name +"님이 초대를 하였습니다.");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkhttpGame okhttpGame = new OkhttpGame();
                String status = null;
                int room_number = Integer.parseInt(room_num);
                Log.d("aaa","aa");
                try {
                    status = okhttpGame.execute("3",room_number).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getActivity(),status, Toast.LENGTH_SHORT).show();
                if (status.equals("pass")) {
//                    Toast.makeText(GameActivity.this, "방에 들어갑니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GameActivity.this, RoomActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("iden", iden);
                    intent.putExtra("room_num", room_num);
                    intent.putExtra("room_name", room_name);
                    startActivity(intent);
                }
                else if (status.equals("nothing")){
                    Toast.makeText(GameActivity.this, "이미 게임을 시작하고있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
//                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    json_row = 1;
                }
                json_row = json_row + 1;
            }
        });
        dialog.show();
    }

    void Dialog_logout(){
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("로그아웃");
        dialog.setMessage("로그아웃 하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new OkhttpToken().execute("2",iden,id);
                Intent intent = new Intent(GameActivity.this,MainActivity.class);
                intentClass.UserLogout(database,intent);
                finish();
//                new OkhttpUser(this,database).execute();
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void switchfragment(int type){

        Bundle bundle = new Bundle();
        if (type == 1){
            fr = new FriendlistFragment();
            bundle.putString("friend_list_json",friend_list_json);
            bundle.putString("check_json",check_json);
            Log.d("listlistlist", friend_list_json);
        }
        else {
/*
            bundle.putString("game_list_json",game_list_json);
            bundle.putInt("game_list_focus",game_list_focus);
*/
            fr = new GamelistFragment();
        }
        bundle.putString("iden",iden);
        bundle.putString("id",id);
        bundle.putString("uri", uri);
        Log.d("switch",iden + id + uri);
        fr.setArguments(bundle);
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentfriendorgame,fr);
        fragmentTransaction.commit();
    }
}
