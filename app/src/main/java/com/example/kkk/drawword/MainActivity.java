package com.example.kkk.drawword;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.Fragment;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements View.OnClickListener{
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
        String id, pwd1,pwd2 ,name,sex;

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
            switch (v.getId()){
                case R.id.joinbutton :
                    id = user_id.getText().toString();
                    pwd1 = user_pwd1.getText().toString();
                    pwd2 = user_pwd2.getText().toString();
                    name = user_name.getText().toString();
                    sex = (String) sex_spinner.getSelectedItem();
                    if (name.equals("") || pwd1.equals("") || id.equals("") || sex.equals("선택")){

                        Toast.makeText(getActivity(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (!pwd1.equals(pwd2)){
                        Toast.makeText(getActivity(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        UserInfoAsync create = new UserInfoAsync(MainActivity.this,database);
                        create.execute("1",id,pwd1,name,"01012341234","12",sex);
                    }
                    break;
                case R.id.join_photo_select :



            }
        }
    }
}
