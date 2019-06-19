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

public class MyTypeListViewAdapter extends BaseAdapter {
    private List<TypeListViewItem> data;
    private Context context;
    private TextChangeListener textChangeListener;

    public interface TextChangeListener{
        void remark_change(String remark);
    }

    public MyTypeListViewAdapter(Context _context, List<TypeListViewItem> list) {
        this.context = _context;
        this.data = list;
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TypeListViewItem item = data.get(i);
        ViewHolderType1 viewHolderType1;
        ViewHolderType2 viewHolderType2;
        int type = getItemViewType(i);
        if (convertView == null) {
            switch (type) {
                case 0:
                    viewHolderType1 = new ViewHolderType1();
                    convertView =  LayoutInflater.from(context).inflate(R.layout.item_payment_type1, null);
                    viewHolderType1.listView = (ListView)convertView.findViewById(R.id.order_list);
                    viewHolderType1.tx_sum = (TextView)convertView.findViewById(R.id.total_count);
                    convertView.setTag(R.id.item_type1, viewHolderType1);
                    break;
                case 1:
                    viewHolderType2 = new ViewHolderType2();
                    convertView =  LayoutInflater.from(context).inflate(R.layout.item_payment_type2, null);
                    viewHolderType2.editText = (EditText)convertView.findViewById(R.id.remark);
                    convertView.setTag(R.id.item_type2, viewHolderType2);
                    break;
            }
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            switch (type){
                case 0:
                    viewHolderType1 = (ViewHolderType1) convertView.getTag(R.id.item_type1);
                    List<Food> order_data = (List<Food>) item.map.get("list_data");
                    MyFinalOrderAdapter myOrderListViewAdapter = new MyFinalOrderAdapter(context, order_data);
                    viewHolderType1.listView.setAdapter(myOrderListViewAdapter);
                    double sum = 0;
                    for(int j = 0; j < order_data.size(); j++){
                        double price = Double.valueOf(order_data.get(j).getFoodPrices());
                        sum += order_data.get(j).getCount() * price;
                    }
                    viewHolderType1.tx_sum.setText("￥" + sum);
                    break;
                case 1:
                    viewHolderType2 = (ViewHolderType2) convertView.getTag(R.id.item_type2);
                    viewHolderType2.editText.setHint("订单备注（辣度，种类...）");
                    viewHolderType2.editText.addTextChangedListener(new TextWatcher() {
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
            }
        }
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolderType1 {
        public ListView listView;
        public TextView tx_sum;
    }

    private class ViewHolderType2 {
        public EditText editText;
    }
}
