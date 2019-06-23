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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


 // 右部滑动头部ListView
public class PinnedListViewAdapter extends BaseAdapter {

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

    public PinnedListViewAdapter(Context context, List<Food> data) {
        this.context = context;
        this.list_data = data;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_food_detail, null);
            viewHolder = new ViewHolder();

            viewHolder.txt_type = (TextView) convertView.findViewById(R.id.txt_food_headers);
            viewHolder.img_food = (ImageView) convertView.findViewById(R.id.img_food);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);
            viewHolder.txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            viewHolder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            viewHolder.btn_sub = (Button) convertView.findViewById(R.id.btn_sub);

            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //内容
        viewHolder.txt_name.setText(list_data.get(position).getName());
        viewHolder.txt_price.setText(list_data.get(position).getPrice());
        if(list_data.get(position).getCount() == 0){
            viewHolder.txt_count.setVisibility(View.INVISIBLE);
            viewHolder.btn_sub.setVisibility(View.GONE);
        }else{
            viewHolder.txt_count.setText(list_data.get(position).getCount() + "");
            viewHolder.txt_count.setVisibility(View.VISIBLE);
            viewHolder.btn_sub.setVisibility(View.VISIBLE);
        }
        //使用获取的图片源修改
        if(!list_data.get(position).getIcon().equals("")) {
            viewHolder.img_food.setImageBitmap(list_data.get(position).getBmIcon());
        }else{
            viewHolder.img_food.setImageResource(R.mipmap.sample_food);
        }

        //设置添加按钮点击事件
        if(onbuttonClickListener == null){
            Log.i("Error", "getView: onbuttonClickListener not found");
        }
        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbuttonClickListener.onAddClick(position);
            }
        });
        viewHolder.btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbuttonClickListener.onSubClick(position);
            }
        });

        if (position == 0) {//如果是第一个  需要显示标题
            viewHolder.txt_type.setVisibility(View.VISIBLE);
            viewHolder.txt_type.setText(list_data.get(position).getType());
            //如果这个标题和上一个不一样   也需要将标题显示出来
        } else if (!TextUtils.equals(list_data.get(position).getType(), list_data.get(position - 1).getType())) {
            viewHolder.txt_type.setVisibility(View.VISIBLE);
            viewHolder.txt_type.setText(list_data.get(position).getType());
        } else {
            viewHolder.txt_type.setVisibility(View.GONE);
        }
        return convertView;
    }

     /**
      * 获取列表头状态
      * @param position int item位置
      * @return int 预先定义好的状态int
      */
    public int getPinnedHeaderState(int position) {
        if (position < 0) {
            return PINNED_HEADER_GONE;
        }
        //当条目标题和上一个标题不同的时候，显示上移
        if (position != 0 && !TextUtils.equals(list_data.get(position).getType(), list_data.get(position + 1).getType())) {
            return PINNED_HEADER_PUSHED_UP;
        }
        return PINNED_HEADER_VISIBLE;
    }

     /**
      * 设置列表头内容
      * @param header View 列表头TextView
      * @param position int item位置
      */
    public void configurePinnedHeader(View header, int position) {
        Food item = (Food) getItem(position);
        if (item != null) {
            if (header instanceof TextView) {
                ((TextView) header).setText(item.getType());
            }
        }
    }
    static class ViewHolder {
        public TextView txt_type;
        public TextView txt_name;
        public TextView txt_price;
        public ImageView img_food;
        public TextView txt_count;
        public Button btn_add;
        public Button btn_sub;
    }
}