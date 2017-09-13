package com.example.kkk.drawword;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class friendlist_fragment extends Fragment{
    @BindView(R.id.friend_list) ListView listView;
    @BindView(R.id.my_photo) ImageView myphoto;
    @BindView(R.id.my_name) TextView myname;
    @BindView(R.id.my_ment) TextView myment;
    Friend_Adapter friend_adapter;
    ArrayList<Friend_Data> item = new ArrayList<Friend_Data>();
    String iden,name,ment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);
        ButterKnife.bind(this,view);
        IntentClass intentClass = new IntentClass(getActivity());
        Database database = new Database(getActivity(),"user_db", null,2);
        iden = null;
        name = null;
        ment = null;
        GetUserInfo(database);
        myname.setText(name);
        myment.setText(ment);
        friend_adapter = new Friend_Adapter(inflater,item);
//        item.add(new Friend_Data("김경관","안녕!!!!!!!!!!1"));
        listView.setAdapter(friend_adapter);

        Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into(myphoto);
        RequestCreator as = Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png");
        Drawable d = myphoto.getDrawable();
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        item.add(new Friend_Data("as","as",b));
//        return inflater.inflate(R.layout.friendlist_fragment, container, false);
        return view;
    }

    void GetUserInfo(Database database){
        iden = database.show_id("select * from user_token",0,1);
        name = database.show_id("select * from user_token",0,2);
        ment = database.show_id("select * from user_token",0,3);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }


    void layout(View view){
        listView = (ListView) view.findViewById(R.id.friend_list);
    }


}
