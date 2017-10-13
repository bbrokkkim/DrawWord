package com.example.kkk.drawword.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.R;

import java.util.ArrayList;

/**
 * Created by KKK on 2017-10-14.
 */

public class DrawAdapter extends BaseAdapter {

    ArrayList<DrawData> item;
    LayoutInflater inflater;

    public DrawAdapter(LayoutInflater inflater, ArrayList<DrawData> item){

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
            convertView = inflater.inflate(R.layout.draw_row, null);
        }

        TextView content = (TextView) convertView.findViewById(R.id.content);

        content.setText(item.get(pos).getContent());

        return convertView;
    }
}
