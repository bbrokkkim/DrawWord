package com.example.kkk.drawword;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-17.
 */

public class gamelist_fragment extends Fragment {
    ListView listView;
    ArrayList<Gamelist_Data> item = new ArrayList<Gamelist_Data>();
    Gamelist_Adapter gamelist_adapter;
    Button create_room;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamelist_fragment,container,false);
        Game_layout(view);

        gamelist_adapter = new Gamelist_Adapter(inflater,item);
        item.add(new Gamelist_Data("1","테스트방입니다","2/4"));
        listView.setAdapter(gamelist_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RoomActivity.class);
                startActivity(intent);
            }
        });

        create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

    void Game_layout(View v){
        listView = (ListView) v.findViewById(R.id.game_list);
        create_room = (Button) v.findViewById(R.id.create_room);
    }


    public class Tcp_Connect extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {

            return null;
        }
    }
}
