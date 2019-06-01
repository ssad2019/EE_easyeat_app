package com.example.lianghw.easyeat;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;


public class MyListViewAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;
    private int selected_position = 0;

    public MyListViewAdapter(Context _context, List<String> list) {
        this.context = _context;
        this.data = list;
    }
    @Override
    public int getCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if(data == null) {
            return null;
        }
        return data.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ListViewHolder viewHolder;
        if (view == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.food_type_item, null);
            viewHolder = new ListViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.type_name);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.textView.setText(data.get(i));
        if(i == selected_position){
            viewHolder.textView.setBackgroundColor(Color.parseColor("#98FB98"));
        }else{
            viewHolder.textView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }
        // 将这个处理好的view返回
        return convertView;
    }
    public void updateData(ArrayList<String> lists) {
        data.clear();
        data.addAll(lists);
        this.notifyDataSetChanged();
    }
    private class ListViewHolder {
        public TextView textView;
    }

    public void changeSelected(int position){
        if (position >= 0 && position < data.size()){
            selected_position = position;
        }
        this.notifyDataSetChanged();
    }
}
