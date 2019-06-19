/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyFinalOrderAdapter extends BaseAdapter {
    private List<Food> data;
    private Context context;
    private int selected_position = 0;

    public MyFinalOrderAdapter(Context _context, List<Food> list) {
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
        MyFinalOrderAdapter.ListViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.final_order_item, null);
            viewHolder = new MyFinalOrderAdapter.ListViewHolder();

            viewHolder.food_img = (ImageView) convertView.findViewById(R.id.food_img);
            viewHolder.food_name = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.food_count = (TextView) convertView.findViewById(R.id.food_count);
            viewHolder.food_total_price = (TextView) convertView.findViewById(R.id.food_prices);

            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (MyFinalOrderAdapter.ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        //根据url设置图片
        //viewHolder.food_img.setImageURI();
        viewHolder.food_name.setText(data.get(i).getName());
        viewHolder.food_count.setText("x" + data.get(i).getCount());
        double one_price = Double.valueOf(data.get(i).getPrice());
        double total_price = one_price * data.get(i).getCount();
        viewHolder.food_total_price.setText("Y" + total_price);

        // 将这个处理好的view返回
        return convertView;
    }
    public void updateData(List<Food> lists) {
        data.clear();
        data.addAll(lists);
        this.notifyDataSetChanged();
    }
    private class ListViewHolder {
        public ImageView food_img;
        public TextView food_name;
        public TextView food_count;
        public TextView food_total_price;
    }

}
