package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kkk.drawword.Data.FriendData;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-17.
 */

public class FriendAdapter extends BaseAdapter {

    ArrayList<FriendData> item;
    Context context;
    int inflater;
    CircleTransform circleTransform;
    public FriendAdapter(int inflater, Context context, ArrayList<FriendData> item){

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
        ViewHolder viewHolder;
        Context context = parent.getContext();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_row, null);
            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.user_photo);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.user_ment = (TextView)convertView.findViewById(R.id.user_ment);
            viewHolder.user_iden = (TextView) convertView.findViewById(R.id.iden);
            viewHolder.friend_row = (LinearLayout) convertView.findViewById(R.id.friend_row);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

//        item = new ArrayList<FriendData>();
//        FriendData friend_data = item.get(pos);


        viewHolder.user_name.setText(item.get(pos).getUser_name());
        viewHolder.user_ment.setText(item.get(pos).getUser_ment());
        viewHolder.user_iden.setText(item.get(pos).getIden());

        if (item.get(pos).getChoice() == true) {
            viewHolder.friend_row.setBackgroundColor(Color.rgb(250,188,162));
        }
        else {
            viewHolder.friend_row.setBackgroundColor(Color.rgb(255,236,228));
        }
        circleTransform = new CircleTransform();
        viewHolder.photo.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(String.valueOf(item.get(pos).getUser_photo())).transform(circleTransform).into(viewHolder.photo);
//        Log.d("PHOTO", item.get(pos).getUser_photo());
        return convertView;
    }
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    public class ViewHolder{
        LinearLayout friend_row;
        ImageView photo;
        TextView user_name;
        TextView user_ment;
        TextView user_iden;
        boolean choice;
    }
}
