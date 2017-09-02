package com.example.kkk.drawword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-08-25.
 */

public class Room_Adapter extends BaseAdapter {
    ArrayList<ChatData> item;
    Context context;
    LayoutInflater inflater;
    public Room_Adapter(LayoutInflater inflater, ArrayList<ChatData> item){
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

        return convertView;
    }
}
