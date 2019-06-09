package com.example.lianghw.easyeat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


 // 右部滑动头部ListView
public class PinnedAdapter extends BaseAdapter {

    //头部隐藏
    public static final int PINNED_HEADER_GONE = 0;
    //显示
    public static final int PINNED_HEADER_VISIBLE = 1;
    //上移
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    private Context context;
    private List<Food> list_data;

     public interface OnButtonClickListener{
         void onAddClick(int position);
         void onSubClick(int position);
     }

     private OnButtonClickListener onbuttonClickListener;

     public void setOnButtonClickListener(OnButtonClickListener onbuttonClickListener) {
         this.onbuttonClickListener = onbuttonClickListener;
     }

    public PinnedAdapter(Context context, List<Food> data) {
        this.context = context;
        this.list_data = data;
    }

    public void updateData(List<Food> data) {
         if(list_data != null){
             list_data.clear();
             list_data.addAll(data);
         }
        else{
             list_data = data;
         }
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
        if(position >= list_data.size()){
            Log.e("position", "getItem: list_data " + list_data.size());
            Log.e("position", "getItem: position " + position);
        }
        return list_data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_detail_item, null);
            viewHolder = new ViewHolder();

            viewHolder.foodType = (TextView) convertView.findViewById(R.id.food_headers);
            viewHolder.foodImg = (ImageView) convertView.findViewById(R.id.food_img);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodPrice = (TextView) convertView.findViewById(R.id.food_prices);
            viewHolder.food_count = (TextView) convertView.findViewById(R.id.food_count);
            viewHolder.add_order_btn = (Button) convertView.findViewById(R.id.add_btn);
            viewHolder.sub_order_btn = (Button) convertView.findViewById(R.id.sub_btn);

            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //内容
        viewHolder.foodName.setText(list_data.get(position).getFoodName());
        viewHolder.foodPrice.setText("$" + list_data.get(position).getFoodPrices());
        if(list_data.get(position).getCount() == 0){
            viewHolder.food_count.setVisibility(View.INVISIBLE);
            viewHolder.sub_order_btn.setVisibility(View.GONE);
        }else{
            viewHolder.food_count.setText(list_data.get(position).getCount() + "");
            viewHolder.food_count.setVisibility(View.VISIBLE);
            viewHolder.sub_order_btn.setVisibility(View.VISIBLE);
        }
        //使用获取的图片源修改
        viewHolder.foodImg.setImageResource(R.mipmap.sample_food);

        //设置添加按钮点击事件
        if(onbuttonClickListener == null){
            Log.i("Error", "getView: onbuttonClickListener not found");
        }
        viewHolder.add_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbuttonClickListener.onAddClick(position);
            }
        });
        viewHolder.sub_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbuttonClickListener.onSubClick(position);
            }
        });

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
        public TextView food_count;
        public Button add_order_btn;
        public Button sub_order_btn;
    }
}