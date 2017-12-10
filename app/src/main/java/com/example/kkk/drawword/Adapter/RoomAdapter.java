package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    String user_id;
    public RoomAdapter(LayoutInflater inflater, ArrayList<ChatData> item,String user_id){
        this.inflater = inflater;
        this.item = item;
        this.user_id = user_id;
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
            convertView = inflater.inflate(R.layout.room_row, null);
            viewHolder = new ViewHolder();
            viewHolder.name =(TextView) convertView.findViewById(R.id.chat_name);
            viewHolder.ment = (TextView) convertView.findViewById(R.id.chat_ment);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ChatData chatData = item.get(pos);




        viewHolder.name.setText(item.get(pos).getUser_name());
        viewHolder.ment.setText(item.get(pos).getMent());

        Log.d("getcount", String.valueOf(getCount()));
        Log.d("count", String.valueOf(count));

        /*
        Log.d("count", String.valueOf(user_id));
        Toast.makeText(context, user_id+ "tetetetetet", Toast.LENGTH_SHORT).show();
        */

        String after = item.get(pos).getUser_name();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT
                , ViewPager.LayoutParams.WRAP_CONTENT);
        if (user_id.equals(after)){
            params.gravity = Gravity.RIGHT;
        }
        else if (!user_id.equals(after)){
            params.gravity = Gravity.LEFT;
        }

        viewHolder.name.setLayoutParams(params);
        viewHolder.ment.setLayoutParams(params);

        if (pos > 0) {
            String before = item.get(pos - 1).getUser_name();
            Log.d("before", before);
            Log.d("after", after);
            if (before.equals(after)){
                viewHolder.name.setVisibility(View.GONE);
            }
            else if (!before.equals(after)){
                viewHolder.name.setVisibility(View.VISIBLE);
            }


        }

        return convertView;
    }
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    public class ViewHolder{
        public int number;
        TextView name;
        TextView ment;
    }
}
