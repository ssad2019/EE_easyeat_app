
package com.example.lianghw.easyeat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract  class MyRecyclerViewAdapter<Food>  extends RecyclerView.Adapter<MyViewHolder>{
    Context context;
    List<Food> data;
    int layoutId;
    public MyRecyclerViewAdapter(Context context,int layoutId,List<Food> data){
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }


    public Food getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = MyViewHolder.get(context, parent, layoutId);
        return holder;

    }
    public abstract void convert(MyViewHolder holder, Food food);
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder.getAdapterPosition());

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(holder.getAdapterPosition());
                    data.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    return true;
                }
            });
        }
        convert(holder, data.get(position)); // convert函数需要重写，下面会讲

    }
    @Override
    public int getItemCount(){
        return data.size();
    }
    private OnItemClickListener onItemClickListener = null;
    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }




}
