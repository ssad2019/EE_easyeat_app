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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static com.example.lianghw.easyeat.TypeListViewItem.TYPELISTITEMVIEW_TYPE_1;
import static com.example.lianghw.easyeat.TypeListViewItem.TYPELISTVIEWITEM_TYPE_2;
import static com.example.lianghw.easyeat.TypeListViewItem.TYPELISTVIEWITEM_TYPE_3;

public class TypeListViewAdapter extends BaseAdapter {
    private List<TypeListViewItem> list_data;
    private Context context;
    private TextChangeListener textChangeListener;

    public interface TextChangeListener{
        void remark_change(String remark);
    }

    public TypeListViewAdapter(Context _context, List<TypeListViewItem> list) {
        this.context = _context;
        this.list_data = list;
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    @Override
    public int getItemViewType(int position) {
        return list_data.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TypeListViewItem item = list_data.get(position);
        ViewHolderType1 viewHolderType1;
        ViewHolderType2 viewHolderType2;
        ViewHolderType3 viewHolderType3;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPELISTITEMVIEW_TYPE_1:
                    viewHolderType1 = new ViewHolderType1();
                    convertView =  LayoutInflater.from(context).inflate(R.layout.item_payment_type1, null);
                    viewHolderType1.lv_order = (ListView)convertView.findViewById(R.id.lv_order);
                    viewHolderType1.txt_sum = (TextView)convertView.findViewById(R.id.txt_total);
                    convertView.setTag(R.id.item_type1, viewHolderType1);
                    break;
                case TYPELISTVIEWITEM_TYPE_2:
                    viewHolderType2 = new ViewHolderType2();
                    convertView =  LayoutInflater.from(context).inflate(R.layout.item_payment_type2, null);
                    viewHolderType2.edit_remark = (EditText)convertView.findViewById(R.id.edit_remark);
                    convertView.setTag(R.id.item_type2, viewHolderType2);
                    break;
                case TYPELISTVIEWITEM_TYPE_3:
                    viewHolderType3 = new ViewHolderType3();
                    convertView =  LayoutInflater.from(context).inflate(R.layout.item_final_type3, null);
                    viewHolderType3.txt_id = (TextView) convertView.findViewById(R.id.txt_order_id);
                    viewHolderType3.txt_time = (TextView) convertView.findViewById(R.id.txt_order_time);
                    convertView.setTag(R.id.item_type3, viewHolderType3);
                    break;
            }
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            switch (type){
                case TYPELISTITEMVIEW_TYPE_1:
                    viewHolderType1 = (ViewHolderType1) convertView.getTag(R.id.item_type1);
                    List<Food> list_data = (List<Food>) item.map.get("list_data");
                    FinalOrderListViewAdapter orderListViewAdapter = new FinalOrderListViewAdapter(context, list_data);
                    viewHolderType1.lv_order.setAdapter(orderListViewAdapter);
                    int int_total_height = 0;
                    for (int i = 0; i < orderListViewAdapter.getCount(); i++) {
                        View listItem = orderListViewAdapter.getView(i, null, viewHolderType1.lv_order);
                        listItem.measure(0, 0);
                        int_total_height += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = viewHolderType1.lv_order.getLayoutParams();
                    params.height = int_total_height + (viewHolderType1.lv_order.getDividerHeight() * (orderListViewAdapter.getCount() - 1)) > DensityUtil.dpToPx(context, 300)
                            ? int_total_height + (viewHolderType1.lv_order.getDividerHeight() * (orderListViewAdapter.getCount() - 1)) : DensityUtil.dpToPx(context, 300);
                    ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
                    viewHolderType1.lv_order.setLayoutParams(params);

                    double sum = 0;
                    for(int j = 0; j < list_data.size(); j++){
                        double price = Double.valueOf(list_data.get(j).getPrice());
                        sum += list_data.get(j).getCount() * price;
                    }
                    viewHolderType1.txt_sum.setText("￥" + sum);
                    break;
                case TYPELISTVIEWITEM_TYPE_2:
                    viewHolderType2 = (ViewHolderType2) convertView.getTag(R.id.item_type2);
                    viewHolderType2.edit_remark.setHint("订单备注（辣度，种类...）");
                    viewHolderType2.edit_remark.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            textChangeListener.remark_change(s.toString());
                        }
                    });
                    break;
                case TYPELISTVIEWITEM_TYPE_3:
                    viewHolderType3 = (ViewHolderType3) convertView.getTag(R.id.item_type3);
                    viewHolderType3.txt_id.setText(item.map.get("order_id").toString());
                    viewHolderType3.txt_time.setText(item.map.get("order_time").toString());
                    break;
            }
        }
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolderType1 {
        public ListView lv_order;
        public TextView txt_sum;
    }

    private class ViewHolderType2 {
        public EditText edit_remark;
    }

    private class ViewHolderType3 {
        public TextView txt_id;
        public TextView txt_time;
    }
}
