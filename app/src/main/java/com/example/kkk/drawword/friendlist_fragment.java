package com.example.kkk.drawword;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class friendlist_fragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.friend_list) ListView listView;
    @BindView(R.id.my_photo) ImageView myphoto;
    @BindView(R.id.my_name) TextView myname;
    @BindView(R.id.my_ment) TextView myment;
    @BindView(R.id.add_friend) Button add_friend;
    Friend_Adapter friend_adapter;
    ArrayList<Friend_Data> item = new ArrayList<Friend_Data>();
    String iden,id,ment,photo_uri,token,friend_list_json;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);
        ButterKnife.bind(this,view);

        IntentClass intentClass = new IntentClass(getActivity());
        Database database = new Database(getActivity(),"user_db", null,1);

        Bundle bundle = new Bundle();
        iden = getArguments().getString("iden");
        id = getArguments().getString("id");
        photo_uri = getArguments().getString("uri");
        friend_list_json = getArguments().getString("friend_list_json");
        //user_info
        myname.setText(id);
        myment.setText("아직 준비 모함");
        String photo_backUri = "http://13.124.229.116/user_photo/";
        Picasso.with(getActivity()).load(photo_backUri+photo_uri).into(myphoto);
        Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
        Log.d("photo_uri",photo_backUri+photo_uri);
        Log.d("photo_uri",id+ iden);

        add_friend.setOnClickListener(this);

        friend_adapter = new Friend_Adapter(inflater,item);
        listView.setAdapter(friend_adapter);

        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend:
                OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("2");
        }
    }
}
