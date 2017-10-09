package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.R;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-25.
 */

public class RoomAdapter extends BaseAdapter {
    ArrayList<ChatData> item;
    Context context;
    LayoutInflater inflater;
    String before = "asdf";
    int count = 1;
    boolean check = true;
    public RoomAdapter(LayoutInflater inflater, ArrayList<ChatData> item){
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
            convertView = inflater.inflate(R.layout.room_row, null);
        }

        ChatData chatData = item.get(pos);

        TextView name = (TextView) convertView.findViewById(R.id.chat_name);
        TextView ment = (TextView) convertView.findViewById(R.id.chat_ment);


        name.setText(item.get(pos).getUser_name());
        ment.setText(item.get(pos).getMent());

        Log.d("getcount", String.valueOf(getCount()));
        Log.d("count", String.valueOf(count));


        if (pos > 0) {
            String before = item.get(pos - 1).getUser_name();
            String after = item.get(pos).getUser_name();
            Log.d("before", before);
            Log.d("after", after);
            if (before.equals(after)){
                name.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
