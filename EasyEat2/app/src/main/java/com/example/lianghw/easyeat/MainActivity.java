package com.example.lianghw.easyeat;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private MyListViewAdapter myListViewAdapter;
    private PinnedAdapter myPinnedAdapter;
    private List<Food> foods;
    private List<String> food_type;
    private boolean scroll_end = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //初始化食品列表
        initFood();

        final ListView food_type_list = (ListView) findViewById(R.id.food_type);
        myListViewAdapter = new MyListViewAdapter(MainActivity.this, food_type);
        food_type_list.setAdapter(myListViewAdapter);

        final PinnedListView food_detail_list = (PinnedListView) findViewById(R.id.food_detail);
        myPinnedAdapter = new PinnedAdapter(MainActivity.this, foods);

        food_detail_list.setAdapter(myPinnedAdapter);
        food_detail_list.setPinnedHeaderView(getHeaderView());

        food_detail_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING){
                    scroll_end = true;
                }else{
                    scroll_end = false;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!scroll_end){
                    return;
                }
                //滚动确定替换条目
                food_detail_list.configureHeaderView(firstVisibleItem);
                //获取到第一个条目的类型
                String title = foods.get(firstVisibleItem).getFoodType();
                int position = food_type.indexOf(title);
                food_type_list.smoothScrollToPosition(position);
                myListViewAdapter.changeSelected(position);
            }
        });

        food_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("nice_gg", "onItemClick: " + foods.get(position).getFoodName());
            }
        });

        food_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < foods.size(); i++){
                    if(foods.get(i).getFoodType().equals(food_type.get(position))){
                        food_detail_list.setSelection(i);
                        break;
                    }
                    myListViewAdapter.changeSelected(position);
                }
            }
        });

        final Button order_make_btn = (Button)findViewById(R.id.order_make_btn);
        order_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,  PayActivity.class);
                startActivity(intent);
            }
        });
    }


    private View getHeaderView() {
        //头部是个TextView，不能用view.inflate加载布局,会测量不出宽高
        TextView itemView = new TextView(this);
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                100));
        itemView.setGravity(Gravity.LEFT);
        itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        itemView.setTextSize(20);
        itemView.setTextColor(Color.GRAY);
        itemView.setPadding(30, 0, 0, itemView.getPaddingBottom());
        return itemView;
    }

    void initFood(){
        final Food chips = new Food("薯条", "小食", "6");
        final Food chips1 = new Food("薯条1", "小食", "6");
        final Food chips2 = new Food("薯条2", "小食", "6");
        final Food chips3 = new Food("薯条3", "小食1", "7");
        final Food chips4 = new Food("薯条4", "小食1", "7");
        final Food chips5 = new Food("薯条5", "小食1", "7");
        final Food chips6 = new Food("薯条6", "小食2", "8");
        final Food chips7 = new Food("薯条7", "小食2", "8");
        final Food chips8 = new Food("薯条8", "小食2", "8");
        final Food chips9 = new Food("薯条9", "小食3", "9");
        final Food chips10 = new Food("薯条10", "小食3", "9");
        final Food chips11 = new Food("薯条11", "小食3", "9");
        final Food chips12 = new Food("薯条12", "小食3", "9");
        final Food chips13 = new Food("薯条13", "小食4", "10");
        final Food chips14 = new Food("薯条14", "小食4", "10");
        final Food chips15 = new Food("薯条15", "小食4", "10");
        final Food chips16 = new Food("薯条16", "小食4", "10");
        final Food chips17 = new Food("薯条17", "小食5", "11");
        final Food chips18 = new Food("薯条18", "小食5", "11");
        final Food chips19 = new Food("薯条19", "小食5", "11");
        final Food chips20 = new Food("薯条20", "小食5", "11");

        foods = new ArrayList<Food>(){{add(chips);add(chips1);add(chips2);add(chips3);add(chips4);add(chips5);add(chips6);add(chips7);add(chips8);add(chips9);add(chips10);
        add(chips11);add(chips12);add(chips13);add(chips14);add(chips15);add(chips16);add(chips17);add(chips18);add(chips19);add(chips20);}};
        food_type = new ArrayList<String>(){{add("小食");add("小食1");add("小食2");add("小食3");add("小食4");add("小食5");}};
    }
}




