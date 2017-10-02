package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkk.drawword.Data.FriendData;
import com.example.kkk.drawword.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-17.
 */

public class FriendAdapter extends BaseAdapter {

    ArrayList<FriendData> item;
    Context context;
    LayoutInflater inflater;

    public FriendAdapter(LayoutInflater inflater, Context context, ArrayList<FriendData> item){

        this.context = context;
        this.inflater = inflater;
        this.item = item;
    }



    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        Context context = parent.getContext();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_row, null);
        }

//        item = new ArrayList<FriendData>();
//        FriendData friend_data = item.get(pos);

        ImageView photo = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView user_name = (TextView) convertView.findViewById(R.id.user_name);
        TextView user_ment = (TextView)convertView.findViewById(R.id.user_ment);
        TextView user_iden = (TextView) convertView.findViewById(R.id.iden);
        user_name.setText(item.get(pos).getUser_name());
        user_ment.setText(item.get(pos).getUser_ment());
        user_iden.setText(item.get(pos).getIden());
        Picasso.with(context).load(String.valueOf(item.get(pos).getUser_photo())).into(photo);

        return convertView;
    }
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}
