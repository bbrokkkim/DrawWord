package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kkk.drawword.Data.GamelistData;
import com.example.kkk.drawword.R;

import java.util.ArrayList;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * Created by KKK on 2017-08-17.
 */

public class GamelistAdapter extends BaseAdapter {
    ArrayList<GamelistData> item;
    LayoutInflater inflater;
    Context context;

    public GamelistAdapter(LayoutInflater inflater, Context context, ArrayList<GamelistData> item){
        this.inflater = inflater;
        this.context = context;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_row, null);
            viewHolder.room_num = (TextView) convertView.findViewById(R.id.room_number);
            viewHolder.room_name = (TextView) convertView.findViewById(R.id.room_name);
            viewHolder.room_con = (TextView) convertView.findViewById(R.id.room_condition);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        GamelistData gamelist_data = item.get(pos);

        Log.d("postion","pos "+ pos );
        viewHolder.room_num.setText(String.valueOf(item.get(pos).getRoom_num()));
        viewHolder.room_name.setText(item.get(pos).getRoom_name());
        viewHolder.room_con.setText(item.get(pos).getRoom_con());

        return convertView;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public class ViewHolder{
        TextView room_num;
        TextView room_name;
        TextView room_con;
    }
}
