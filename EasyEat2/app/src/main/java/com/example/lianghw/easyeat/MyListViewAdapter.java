package com.example.lianghw.easyeat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class MyListViewAdapter extends BaseAdapter {

    private List<Food> mData;
    private LayoutInflater mInflater;

    public void refresh(Food data){
        mData.add(data);
        notifyDataSetChanged();
    }
    public void refresh(int id){
        mData.remove(id);
        notifyDataSetChanged();
    }
    public MyListViewAdapter(List<Food> data,LayoutInflater inflater){
        mData = data;
        mInflater = inflater;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int position) {
        if (mData == null) {
            return null;
        }
        return mData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.food_item, null);
            viewHolder = new ViewHolder();
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodType_short = (TextView) convertView.findViewById(R.id.food_type);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Food myfood = mData.get(position);


        viewHolder.foodName.setText(myfood.getFoodName());
        viewHolder.foodType_short.setText(myfood.getFoodType_short());

        return convertView;

    }
    private class ViewHolder {
        public TextView foodName;
        public TextView foodType_short;
    }


}
