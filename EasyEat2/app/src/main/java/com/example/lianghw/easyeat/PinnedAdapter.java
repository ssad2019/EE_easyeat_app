package com.example.lianghw.easyeat;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by oy 2017/7/28 10:43.
 * 右部滑动头部ListView
 */
public class PinnedAdapter extends BaseAdapter {
    //头部隐藏
    public static final int PINNED_HEADER_GONE = 0;
    //显示
    public static final int PINNED_HEADER_VISIBLE = 1;
    //上移
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    private Context context;
    private List<Food> list_data;
    public PinnedAdapter(Context context, List<Food> data) {
        this.context = context;
        this.list_data = data;
    }

    public void updateData(ArrayList<Food> data) {
        list_data.clear();
        list_data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list_data == null){
            return 0;
        }
        return list_data.size();
    }
    @Override
    public Object getItem(int position) {
        if(list_data == null) {
            return null;
        }
        return list_data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.foodType = (TextView) convertView.findViewById(R.id.food_headers);
            viewHolder.foodImg = (ImageView) convertView.findViewById(R.id.food_img);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodPrice = (TextView) convertView.findViewById(R.id.food_prices);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //内容
        viewHolder.foodName.setText(list_data.get(position).getFoodName());
        viewHolder.foodPrice.setText(list_data.get(position).getFoodPrices());
        //使用获取的图片源修改
        viewHolder.foodImg.setImageResource(R.mipmap.sample_food);

        if (position == 0) {//如果是第一个  需要显示标题
            viewHolder.foodType.setVisibility(View.VISIBLE);
            viewHolder.foodType.setText(list_data.get(position).getFoodType());
            //如果这个标题和上一个不一样   也需要将标题显示出来
        } else if (!TextUtils.equals(list_data.get(position).getFoodType(), list_data.get(position - 1).getFoodType())) {
            viewHolder.foodType.setVisibility(View.VISIBLE);
            viewHolder.foodType.setText(list_data.get(position).getFoodType());
        } else {
            viewHolder.foodType.setVisibility(View.GONE);
        }
        return convertView;
    }
    //获取HeaderView的状态
    public int getPinnedHeaderState(int position) {
        if (position < 0) {
            return PINNED_HEADER_GONE;
        }
        //当条目标题和上一个标题不同的时候，显示上移
        if (position != 0 && !TextUtils.equals(list_data.get(position).getFoodType(), list_data.get(position + 1).getFoodType())) {
            return PINNED_HEADER_PUSHED_UP;
        }
        return PINNED_HEADER_VISIBLE;
    }
    //设置HeaderView的内容
    public void configurePinnedHeader(View header, int position) {
        Food item = (Food) getItem(position);
        if (item != null) {
            if (header instanceof TextView) {
                ((TextView) header).setText(item.getFoodType());
            }
        }
    }
    static class ViewHolder {
        public TextView foodType;
        public TextView foodName;
        public TextView foodPrice;
        public ImageView foodImg;
    }
}