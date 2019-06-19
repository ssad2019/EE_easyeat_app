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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyOrderListViewAdapter extends BaseAdapter {
    private List<Food> data;
    private Context context;

    public interface OnOrderButtonClickListener{
        void onAddClick(int position);
        void onSubClick(int position);
    }

    private OnOrderButtonClickListener onOrderButtonClickListener;

    public void setOnOrderButtonClickListener(OnOrderButtonClickListener onbuttonClickListener) {
        this.onOrderButtonClickListener = onbuttonClickListener;
    }

    public MyOrderListViewAdapter(Context _context, List<Food> list) {
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View convertView;
        MyOrderListViewAdapter.ListViewHolder viewHolder;
        if (view == null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.order_item, null);
            viewHolder = new MyOrderListViewAdapter.ListViewHolder();

            viewHolder.foodImg = (ImageView) convertView.findViewById(R.id.food_img);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodPrice = (TextView) convertView.findViewById(R.id.food_prices);
            viewHolder.food_count = (TextView) convertView.findViewById(R.id.food_count);
            viewHolder.add_btn = (Button) convertView.findViewById(R.id.add_btn);
            viewHolder.sub_btn = (Button) convertView.findViewById(R.id.sub_btn);

            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (MyOrderListViewAdapter.ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.foodName.setText(data.get(position).getName());
        viewHolder.foodPrice.setText(data.get(position).getPrice());
        viewHolder.food_count.setText(data.get(position).getCount() + "");

        viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderButtonClickListener.onAddClick(position);
            }
        });
        viewHolder.sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderButtonClickListener.onSubClick(position);
            }
        });

        //设置图片url

        // 将这个处理好的view返回
        return convertView;
    }
    public void updateData(List<Food> lists) {
        data.clear();
        data.addAll(lists);
        this.notifyDataSetChanged();
    }
    private class ListViewHolder {
        public TextView foodName;
        public ImageView foodImg;
        public TextView foodPrice;
        public TextView food_count;
        public Button add_btn;
        public Button sub_btn;
    }
}
