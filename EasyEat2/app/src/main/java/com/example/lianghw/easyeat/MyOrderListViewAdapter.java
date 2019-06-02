package com.example.lianghw.easyeat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyOrderListViewAdapter extends BaseAdapter {
    private List<Order_Food> data;
    private Context context;

    public MyOrderListViewAdapter(Context _context, List<Order_Food> list) {
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
        MyOrderListViewAdapter.ListViewHolder viewHolder;
        if (view == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.order_item, null);
            viewHolder = new MyOrderListViewAdapter.ListViewHolder();
            viewHolder.foodImg = (ImageView) convertView.findViewById(R.id.food_img);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodPrice = (TextView) convertView.findViewById(R.id.food_prices);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (MyOrderListViewAdapter.ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.foodName.setText(data.get(i).getFoodName());
        viewHolder.foodPrice.setText(data.get(i).getFoodPrices());
        //设置图片url

        // 将这个处理好的view返回
        return convertView;
    }
    public void updateData(ArrayList<Order_Food> lists) {
        data.clear();
        data.addAll(lists);
        this.notifyDataSetChanged();
    }
    private class ListViewHolder {
        public TextView foodName;
        public ImageView foodImg;
        public TextView foodPrice;
    }
}