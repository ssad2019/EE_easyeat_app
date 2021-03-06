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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lianghw.easyeat.Restaurant.makeRestaurantByUrl;

public class FinalOrderListViewAdapter extends BaseAdapter {
    private List<Food> list_data;
    private Context context;
    private static final int HANDLER_MESSAGE = 0x01;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE:
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public FinalOrderListViewAdapter(Context _context, List<Food> list) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView;
        FinalOrderListViewAdapter.ListViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_final_order, null);
            viewHolder = new FinalOrderListViewAdapter.ListViewHolder();

            viewHolder.img_food = (ImageView) convertView.findViewById(R.id.img_food);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            viewHolder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);

            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (FinalOrderListViewAdapter.ListViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.txt_name.setText(list_data.get(position).getName());
        viewHolder.txt_count.setText("x" + list_data.get(position).getCount());
        double one_price = Double.valueOf(list_data.get(position).getPrice());
        double total_price = one_price * list_data.get(position).getCount();
        viewHolder.txt_price.setText("￥" + total_price);
        if(!list_data.get(position).getIcon().equals("") && StoreData.isUrl(list_data.get(position).getIcon())) {
            if(list_data.get(position).getBitmap() == null){
                final int int_position = position;
                StoreData.getInstance().threadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        list_data.get(int_position).getBitmapbyUrl();
                        handler.sendEmptyMessage(HANDLER_MESSAGE);
                    }
                });
            }else{
                viewHolder.img_food.setImageBitmap(list_data.get(position).getBitmap());
            }
        }else{
            viewHolder.img_food.setImageResource(R.mipmap.sample_food);
        }
        // 将这个处理好的view返回
        return convertView;
    }


    private class ListViewHolder {
        public ImageView img_food;
        public TextView txt_name;
        public TextView txt_count;
        public TextView txt_price;
    }

}
