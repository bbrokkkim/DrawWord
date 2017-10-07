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
    String name;
    public RoomAdapter(LayoutInflater inflater, ArrayList<ChatData> item, String name){
        this.inflater = inflater;
        this.item = item;
        this.name = name;
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
        check = item.get(pos).isCheck();

        if (check = true){
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();

        Log.d("getcount", String.valueOf(getCount()));
        Log.d("count", String.valueOf(count));


//        if (check == true){
            if (pos > 0) {
                String before = item.get(pos - 1).getUser_name();
                String after = item.get(pos).getUser_name();
                Log.d("before", before);
                Log.d("after", after);
                if (before.equals(after)){
                    name.setVisibility(View.GONE);
                }
            }
//        }
/*        if (after.equals(before)){
            name.setVisibility(convertView.GONE);
        }*/
        /*if (asdf.equals("test1")){
            Toast.makeText(context, "QQQQQQQ", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "WWWWWW", Toast.LENGTH_SHORT).show();
        */
        

        return convertView;
    }
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
