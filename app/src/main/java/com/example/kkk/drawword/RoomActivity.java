package com.example.kkk.drawword;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-17.
 */

public class RoomActivity extends Activity {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList();

    EditText text,port_num;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_layout);




    }
}
