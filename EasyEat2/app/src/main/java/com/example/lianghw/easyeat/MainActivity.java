package com.example.lianghw.easyeat;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private MyListViewAdapter myListViewAdapter;
    private List<Food> foods;
    //private ListView myListView;
    private RecyclerView myRecyclerView1;
    private RecyclerView myRecyclerView2;

    private Food recommendFood;

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
        foods = new ArrayList<Food>();
        initFood();


        //食品列表
        final MyRecyclerViewAdapter myRecyclerViewAdapter1 = new MyRecyclerViewAdapter<Food>(this, R.layout.food_item, foods) {
            @Override
            public void convert(MyViewHolder holder, Food food) {
                TextView name = holder.getView(R.id.food_name);
                TextView type = holder.getView(R.id.food_type);
                type.setText(food.getFoodType_short());
                name.setText(food.getFoodName());
            }
        };


        myRecyclerView1 = (RecyclerView) findViewById(R.id.foodlist);
        //列表点击事件,进入详情界面
        myRecyclerViewAdapter1.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick( int position) {
                //Intent Detail = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("food",(Food)myRecyclerViewAdapter1.getItem(position));
                //Detail.putExtras(bundle);//将bundle传入intent中。
                //startActivityForResult(Detail,1000);
            }
            @Override
            public void onLongClick( int position) {

            }
        });

        myRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView1.setAdapter(myRecyclerViewAdapter1);
        myRecyclerViewAdapter1.notifyDataSetChanged();


        List<Food> foods_collect;
        //收藏夹
        foods_collect = new ArrayList<>();
        foods_collect.add(new Food("收藏夹","*"));

        LayoutInflater inflater =getLayoutInflater();

        //食品列表
        final MyRecyclerViewAdapter myRecyclerViewAdapter2 = new MyRecyclerViewAdapter<Food>(this, R.layout.food_item, foods_collect) {
            @Override
            public void convert(MyViewHolder holder, Food food) {
                TextView name = holder.getView(R.id.food_name);
                TextView type = holder.getView(R.id.food_type);
                type.setText(food.getFoodType_short());
                name.setText(food.getFoodName());
            }
        };
        myRecyclerView2 = (RecyclerView) findViewById(R.id.cartlist);
        myRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView2.setAdapter(myRecyclerViewAdapter2);
        myRecyclerView2.setVisibility(View.INVISIBLE);

        myListViewAdapter = new MyListViewAdapter(foods_collect,inflater);

        Button payBtn = findViewById(R.id.pay_btn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pay = new Intent(MainActivity.this,PayActivity.class);

                startActivityForResult(pay,1000);
            }
        });

        //右下角按钮
        final FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.radio_button);
        //按钮点击
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myRecyclerView1.getVisibility() == View.VISIBLE) {

                    myRecyclerView1.setVisibility(View.INVISIBLE);

                    myRecyclerView2.setVisibility(View.VISIBLE);
                    btn.setImageResource(R.mipmap.mainpage);
                }
                else{

                    myRecyclerView2.setVisibility(View.INVISIBLE);

                    myRecyclerView1.setVisibility(View.VISIBLE);
                    btn.setImageResource(R.mipmap.collect);

                }

            }
        });

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //发送静态广播
        Bundle bundle = new Bundle();
        bundle.putSerializable("food",recommendFood);

        Intent widgetIntentBroadcast = new Intent();
        widgetIntentBroadcast.setAction("WIDGETSTATICACTION");
        widgetIntentBroadcast.putExtras(bundle);
        sendBroadcast(widgetIntentBroadcast);

        Log.d("Restart","Restart");
    }

    void initFood(){
        foods.add(new  Food("大豆", "粮食", "粮", "蛋白质",getResources().getColor(R.color.daDou)));
        foods.add(new Food("十字花科蔬菜", "蔬菜", "蔬", "维生素C",getResources().getColor(R.color.shiZhi )));
        foods.add(new Food("牛奶", "饮品", "饮", "钙",getResources().getColor(R.color.niuNai )));
        foods.add(new Food("海鱼", "肉食", "肉", "蛋白质",getResources().getColor(R.color.haiYv )));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra("Favorites")){
            myRecyclerView2.setVisibility(View.VISIBLE);
            myRecyclerView1.setVisibility(View.INVISIBLE);
        }else{
            myRecyclerView1.setVisibility(View.VISIBLE);
            myRecyclerView2.setVisibility(View.INVISIBLE);

        }
    }


    //处理详情返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }





}




