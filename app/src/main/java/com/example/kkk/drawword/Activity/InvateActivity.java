package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kkk.drawword.Adapter.FriendAdapter;
import com.example.kkk.drawword.Data.FriendData;
import com.example.kkk.drawword.Fragment.FriendlistFragment;
import com.example.kkk.drawword.Okhttp.OkhttpInvate;
import com.example.kkk.drawword.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-11-22.
 */

public class InvateActivity extends Activity {

    @BindView(R.id.invate_btn)
    Button invate_btn;
    @BindView(R.id.invate_list)
    ListView listView;

    FriendAdapter friend_adapter;
    String id,room_num,room_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invate_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("user_name");
        room_name = intent.getStringExtra("room_name");
        room_num = intent.getStringExtra("room_num");


        final ArrayList<FriendData> item = FriendlistFragment.item_static;
        Log.d("item", String.valueOf(item.size()));
        friend_adapter = new FriendAdapter(android.R.layout.simple_list_item_multiple_choice,this,item);
        listView.setAdapter(friend_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.get(position).setChoice();
                Toast.makeText(InvateActivity.this, String.valueOf(item.get(position).getChoice()), Toast.LENGTH_SHORT).show();
                friend_adapter.notifyDataSetChanged();
            }
        });

        invate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                JSONArray list = new JSONArray();

                for (int i = 0; i < item.size(); i++) {
                    if (item.get(i).getChoice() == true){
                        Log.d("list",item.get(i).getUser_name());
                        list.put(item.get(i).getUser_name());


                    }
                }
                try {
                    jsonObject.put("invate_list",list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new OkhttpInvate().execute("2",id,room_num,room_name,jsonObject.toString());
                Log.d("json array",jsonObject.toString());
                finish();
            }
        });

    }
}
