package com.example.kkk.drawword.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.Toast;


import com.example.kkk.drawword.Database;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.ModelLogin;
import com.example.kkk.drawword.Okhttp.OkhttpUser;
import com.example.kkk.drawword.R;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends Activity implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @BindView(R.id.back_btn) ImageButton back;
    @BindView(R.id.other_frag) Button join_btn;
    public static boolean where = true;
    boolean active = false;
    public static Database database;
    IntentClass intentClass;
    Join_fragment join = new Join_fragment();
    Login_fragment login = new Login_fragment();
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    public static String server_url = "http://13.125.101.76/";
    public static String sokcet_url = "13.125.101.76";
    public MainActivity (){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        database = new Database(getApplicationContext(), "user_db", null,1);

        join_btn.setText("회원가입으로");
        final String check_login_info = "uncorrect";
        final ModelLogin model_login = new ModelLogin(check_login_info);

        //fragment

        switchfragment(true);

        intentClass = new IntentClass(MainActivity.this);

        Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
        int count = intentClass.UserCount(database);
        Log.d("count11",String.valueOf(count));
        if (count != 0){
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            intentClass.PushUserInfo(intent);
            finish();
        }

        join_btn.setOnClickListener(this);
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


    public void switchfragment(boolean where){

        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();


        if (where == true){
//            fragmentTransaction.add(R.id.fragmentloginorjoin,login);
            fragmentTransaction.replace(R.id.fragmentloginorjoin,login);
        }
        else if (where == false) {
//            fragmentTransaction.add(R.id.fragmentloginorjoin,join);
            fragmentTransaction.replace(R.id.fragmentloginorjoin,join);
        }


        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                if (where == true){
                    switchfragment(true);
                    join_btn.setVisibility(View.VISIBLE);
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
                where = false;
                switchfragment(false);
                join_btn.setVisibility(View.GONE);
                active = false;
                break;
        }
    }


    //로그인 프레그먼트
    public class Login_fragment extends Fragment {
        @BindView(R.id.login) Button login;
        @BindView(R.id.ed_id) EditText id;
        @BindView(R .id.ed_password) EditText pwd;
        String overlap = "";
        public Login_fragment(){}
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
                    OkhttpUser okhttpUser = new OkhttpUser(MainActivity.this,database);
                    okhttpUser.execute("2",id_btn,pwd_btn);
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
        @BindView(R.id.joinbutton) Button submit;
        @BindView(R.id.back_photo) Button back_button;
        @BindView(R.id.choice_sex) Spinner sex_spinner;
        @BindView(R.id.join_photo) ImageView photo;
        @BindView(R.id.join_photo_select) Button photo_select;
        String id, pwd1,pwd2 ,name,sex;
        int a = 1;
        int REQ_CODE_SELECT_IMAGE = 1;
        Uri uri;
        String real_uri;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.join_fragment,container,false);

            ButterKnife.bind(this,view);
            submit.setOnClickListener(this);
            photo_select.setOnClickListener(this);
            back_button.setOnClickListener(this);
            where = true;
            return view;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //회원가입 완료
                case R.id.joinbutton:
                    id = user_id.getText().toString();
                    pwd1 = user_pwd1.getText().toString();
                    pwd2 = user_pwd2.getText().toString();
                    name = user_name.getText().toString();
                    sex = (String) sex_spinner.getSelectedItem();
                    JoinMent(id,pwd1,pwd2,name,sex);
                    break;
                //이미지 선택
                case R.id.join_photo_select:

                        Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);

                    //마쉬멜로우 이상 권한 얻기
                    if(PermissionStatus(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        PermissionGet();
                    }
                    else {
                        PermissionGet();
                    }
                    break;
                //이미지 초기화
                case R.id.back_photo :
                    Toast.makeText(MainActivity.this, "이미지가 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
                    uri = null;
                    photo.setImageResource(0);

                    break;
            }
        }

        //회원가입 유효성 검사
        void JoinMent(String id,String pwd1, String pwd2, String name,String sex){
            if (name.equals("") || pwd1.equals("") || id.equals("") || sex.equals("남/녀")) {
                Toast.makeText(getActivity(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (!pwd1.equals(pwd2)) {
                Toast.makeText(getActivity(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            } else {
                if (uri == null){
                    a = 1;
                    real_uri = "null";
                }
                OkhttpUser okhttpUser = new OkhttpUser(MainActivity.this, database);

                okhttpUser.execute("1", id, pwd1, name, "01012341234", "12", sex, real_uri);
            }
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == REQ_CODE_SELECT_IMAGE) {
                if(resultCode == Activity.RESULT_OK) {
                    photo.setImageURI(data.getData());
                    uri = data.getData();

                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(uri, projection, null, null, null);
                    startManagingCursor(cursor);
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    real_uri = cursor.getString(columnIndex);
                }
            }
        }
}




    Boolean PermissionStatus(String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, permission);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            return true;
        }
        else{
            // 권한 있음
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
            }
            else {
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
                    // 권한 허가
                // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }
}





