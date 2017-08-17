package com.example.kkk.drawword;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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
        return view;
    }

    void Game_layout(View v){
        listView = (ListView) v.findViewById(R.id.game_list);
        create_room = (Button) v.findViewById(R.id.create_room);
    }
}
