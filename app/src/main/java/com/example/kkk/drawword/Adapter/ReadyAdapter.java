package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkk.drawword.Data.FriendData;
import com.example.kkk.drawword.Data.ReadyData;
import com.example.kkk.drawword.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by KKK on 2017-10-09.
 */

public class ReadyAdapter extends BaseAdapter {
    ArrayList<ReadyData> item;
    Context context;
    LayoutInflater inflater;
    public ReadyAdapter(LayoutInflater inflater, Context context, ArrayList<ReadyData> item){
        this.item = item;
        this.context = context;
        this.inflater = inflater;
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
        Context context = parent.getContext();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ready_row, null);
        }

        TextView user = (TextView) convertView.findViewById(R.id.user);
        ImageView status = (ImageView) convertView.findViewById(R.id.status);

        user.setText(item.get(position).getUser());
        String status_type = item.get(position).getStatus();
        if (status_type.equals("wait")){
            status.setImageResource(0);
        }
        else
            status.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }
}
