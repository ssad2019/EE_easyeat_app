/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import android.content.Context;


public class SimpleListViewAdapter extends BaseAdapter {
    private List<String> list_data;
    private Context context;
    private int int_selected_position = 0;

    public SimpleListViewAdapter(Context _context, List<String> list) {
        this.context = _context;
        this.list_data = list;
    }
    @Override
    public int getCount() {
        if (list_data == null){
            return 0;
        }
        return list_data.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if(list_data == null) {
            return null;
        }
        return list_data.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ListViewHolder viewHolder;
        if (view == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_food_type, null);
            viewHolder = new ListViewHolder();
            viewHolder.txt_type = (TextView) convertView.findViewById(R.id.txt_type_name);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.txt_type.setText(list_data.get(i));
        if(i == int_selected_position){
            viewHolder.txt_type.setBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.txt_type.setTextColor(Color.parseColor("#000000"));
        }else{
            viewHolder.txt_type.setBackgroundColor(Color.parseColor("#FAFAFA"));
            viewHolder.txt_type.setTextColor(Color.parseColor("#8B8682"));
        }
        // 将这个处理好的view返回
        return convertView;
    }

    private class ListViewHolder {
        public TextView txt_type;
    }

    public void changeSelected(int position){
        if (position >= 0 && position < list_data.size()){
            int_selected_position = position;
        }
        this.notifyDataSetChanged();
    }
}
