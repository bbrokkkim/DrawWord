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

public class friendlist_fragment extends Fragment{
    ListView listView;
    Friend_Adapter friend_adapter;
    ArrayList<Friend_Data> item = new ArrayList<Friend_Data>();
    public friendlist_fragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);

        layout(view);

        friend_adapter = new Friend_Adapter(inflater,item);
        item.add(new Friend_Data("김경관","안녕!!!!!!!!!!1"));
        listView.setAdapter(friend_adapter);


//        return inflater.inflate(R.layout.friendlist_fragment, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }


    void layout(View view){
        listView = (ListView) view.findViewById(R.id.friend_list);
    }


}
