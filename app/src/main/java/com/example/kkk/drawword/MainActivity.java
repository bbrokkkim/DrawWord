package com.example.kkk.drawword;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @BindView(R.id.back_btn) ImageButton back;
    @BindView(R.id.other_frag) Button join;
    boolean where = false;
    boolean active = false;
    Database database;
    IntentClass intentClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        database = new Database(getApplicationContext(), "user_db", null,2);

        join.setText("회원가입으로");
        final String check_login_info = "uncorrect";
        final Model_login model_login = new Model_login(check_login_info);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentloginorjoin,new Login_fragment());
        fragmentTransaction.replace(R.id.fragmentloginorjoin,new Login_fragment());
        fragmentTransaction.commit();

        intentClass = new IntentClass(MainActivity.this);

        Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
        int count = intentClass.UserCount(database);
        Log.d("count11",String.valueOf(count));
        if (count != 0){
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            intentClass.PushUserInfo(intent,database);
        }

        join.setOnClickListener(this);
        back.setOnClickListener(this);

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Boolean check = model_login.Connect_http(user_id,user_pwd);

                model_login.connect_login1.execute(user_id,user_pwd);
                if (check_login_info.equals("correct")){
                    Intent intent = new Intent();
                    Toast.makeText(MainActivity.this, "정보일치", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "정보가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_fragment exampleFragment = new Login_fragment();
             //   Login_fragment.onCreateView();
            }
        });*/
    }


    public void switchfragment(){
        Fragment fr,hide_fr;


        if (where == true){
            fr = new Login_fragment();
        }
        else {
            fr = new Join_fragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentloginorjoin,fr);
        fragmentTransaction.replace(R.id.fragmentloginorjoin,fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                if (where == true){
                    switchfragment();
                    join.setVisibility(View.VISIBLE);
                    where = false;
                }
                else if (where == false && active == false){
                    Toast.makeText(this, "한번 더 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show();
                    active = true;
                }
                else if (active == true){
                    finish();
                }
                break;
            case R.id.other_frag :
                switchfragment();
                join.setVisibility(View.GONE);
                where = true;
                active = false;
                break;
        }
    }


    //로그인 프레그먼트
    public class Login_fragment extends Fragment {
        @BindView(R.id.login) Button login;
        @BindView(R.id.ed_id) EditText id;
        @BindView(R.id.ed_password) EditText pwd;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.login_fragment,container,false);
            ButterKnife.bind(this,view);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id_btn = id.getText().toString();
                    String pwd_btn = pwd.getText().toString();

                    UserInfoAsync userInfoAsync = new UserInfoAsync(MainActivity.this,database);
                    userInfoAsync.execute("2",id_btn,pwd_btn);
                }
            });



            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }

    //가입 프레그먼트
    public class Join_fragment extends Fragment implements View.OnClickListener{
        @BindView(R.id.id) EditText user_id;
        @BindView(R.id.password1) EditText user_pwd1;
        @BindView(R.id.password2) EditText user_pwd2;
        @BindView(R.id.name) EditText user_name;
        @BindView(R.id.phone) EditText user_phone;
        @BindView(R.id.write_certification) EditText user_cer;
        @BindView(R.id.check_certification) Button certi_bin;
        @BindView(R.id.joinbutton) Button submit;
        @BindView(R.id.choice_sex) Spinner sex_spinner;
        @BindView(R.id.join_photo) ImageView photo;
        @BindView(R.id.join_photo_select) Button photo_select;
        @BindView(R.id.test_photo) ImageView test;
        String id, pwd1,pwd2 ,name,sex;
        int a = 1;
        IntentClass intentClass;
        int REQ_CODE_SELECT_IMAGE = 1;
        Uri uri;
        public Join_fragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.join_fragment,container,false);

            ButterKnife.bind(this,view);
            submit.setOnClickListener(this);
            photo_select.setOnClickListener(this);
            return view;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.joinbutton:
                    id = user_id.getText().toString();
                    pwd1 = user_pwd1.getText().toString();
                    pwd2 = user_pwd2.getText().toString();
                    name = user_name.getText().toString();
                    sex = (String) sex_spinner.getSelectedItem();
                    if (name.equals("") || pwd1.equals("") || id.equals("") || sex.equals("선택")) {

                        Toast.makeText(getActivity(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (!pwd1.equals(pwd2)) {
                        Toast.makeText(getActivity(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        UserInfoAsync create = new UserInfoAsync(MainActivity.this, database);
                        create.execute("1", id, pwd1, name, "01012341234", "12", sex);
                    }
                    break;
                case R.id.join_photo_select:
                    Drawable drawable = null;
                    Bitmap bitmap = null;
                    if (a == 1) {
                        /*Picasso.with(MainActivity.this).load("http://i.imgur.com/DvpvklR.png").into(photo);

                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        a = 2;*/
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        context.startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                        startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);

                        if(PermissionStatus(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            PermissionGet();
                        }
                        else {
                            PermissionGet();
                        }
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                            Uri uri = intent.getData();
                            Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            photo.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        a = 2;

                    }
                    else if (a == 2){
                        Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                        drawable = photo.getDrawable();
                        bitmap = ((BitmapDrawable)drawable).getBitmap();
                        test.setImageBitmap(bitmap);
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        UserInfoAsync test = new UserInfoAsync(MainActivity.this, database);
                        test.execute("3",bitmap );
                    }





            }
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {


            Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

            if(requestCode == REQ_CODE_SELECT_IMAGE)
            {
                if(resultCode == Activity.RESULT_OK){
                    photo.setImageURI(data.getData());
                    uri = data.getData();
                    Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
}


    Boolean PermissionStatus(String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, permission);

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
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                /*AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
                alt_bld.setTitle("권한");
                alt_bld.setMessage("갤러리에 대한 접근을 승낙하시겠습니까");*//**//*
                alt_bld.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "승낙", Toast.LENGTH_SHORT).show();
                    }
                });
                alt_bld.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "못써 새꺄", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(MainActivity.this, "승낙!!", Toast.LENGTH_SHORT).show();
*/

            } else {
                Toast.makeText(this, "승낙?", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
                    Log.d("aa","1111");
                    // 권한 허가
// 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    Toast.makeText(this, "2222", Toast.LENGTH_SHORT).show();
                    Log.d("aa","2222");
                    // 권한 거부
// 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }
}





