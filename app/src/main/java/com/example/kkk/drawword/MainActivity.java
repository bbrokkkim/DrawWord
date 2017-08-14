package com.example.kkk.drawword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText id,pwd;
    Button login,join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        layout();
        final String user_id = id.getText().toString();
        final String user_pwd = pwd.getText().toString();
//        Presenter_login presenter_login = new Presenter_login(user_id,user_pwd);
        final Model_login model_login = new Model_login();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = model_login.Connect_http(user_id,user_pwd);
                if (check == true){
                    Intent intent = new Intent();
                }
                else{
                    Toast.makeText(MainActivity.this, "정보가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    void layout(){
        id = (EditText)findViewById(R.id.id);
        pwd = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        join = (Button) findViewById(R.id.join);
    }
}
