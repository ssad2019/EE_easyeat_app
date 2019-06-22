/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */package com.example.lianghw.easyeat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class PinnedListView extends ListView {
    //用来判断HeaderView是否需要绘制
    private boolean bln_is_headerViewVisible;
    private View view_header;
    private int int_headerViewWidth;
    private int int_headerViewHeight;
    private PinnedListViewAdapter pinnedListViewAdapter;
    public PinnedListView(Context context) {
        this(context,null);
    }
    public PinnedListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public PinnedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setPinnedHeaderView(View headerView) {
        //获取到HeaderView和Adapter对象
        view_header = headerView;
        pinnedListViewAdapter = (PinnedListViewAdapter) getAdapter();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (view_header != null) {
            //测量HeaderView的宽高
            measureChild(view_header, widthMeasureSpec, heightMeasureSpec);
            //宽
            int_headerViewWidth = view_header.getMeasuredWidth();
            //高
            int_headerViewHeight = view_header.getMeasuredHeight();
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //摆放HeaderView
        if (view_header != null) {
            //layout摆放的位置（左，上，右，下）
            view_header.layout(0, 0, int_headerViewWidth, int_headerViewHeight);
            //根据listview第一个条目的postion,确定HeaderView隐藏/显示/上移
            configureHeaderView(getFirstVisiblePosition());
        }
    }
    public void configureHeaderView(int position) {
        if (view_header == null || null == pinnedListViewAdapter) {
            return;
        }
        int state = pinnedListViewAdapter.getPinnedHeaderState(position);
        switch (state) {
            case PinnedListViewAdapter.PINNED_HEADER_GONE: {
                //如果是不可见的状态，就不绘制HeaderView
                bln_is_headerViewVisible = false;
                break;
            }
            case PinnedListViewAdapter.PINNED_HEADER_VISIBLE: {
                //如果当前是可见的状态，设置HeaderView的内容
                pinnedListViewAdapter.configurePinnedHeader(view_header, position);
                //将被隐藏的HeaderView(在界面外绘制)摆放到顶部
                if (view_header.getTop() != 0) {
                    view_header.layout(0, 0, int_headerViewWidth, int_headerViewHeight);
                }
                //绘制HeaderView
                bln_is_headerViewVisible = true;
                break;
            }
            case PinnedListViewAdapter.PINNED_HEADER_PUSHED_UP: {
                //获取第一个view
                View firstView = getChildAt(0);
                //返回View自身底边到父布局顶边的距离
                int bottom = firstView.getBottom();
                //HeaderView的高度
                int headerHeight = view_header.getHeight();
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                } else {
                    y = 0;
                }
                //绘制HeaderView的内容
                pinnedListViewAdapter.configurePinnedHeader(view_header, position);
                //摆放HeaderView
                if (view_header.getTop() != y) {
                    //HeaderView上移y
                    view_header.layout(0, y, int_headerViewWidth, int_headerViewHeight + y);
                }
                bln_is_headerViewVisible = true;
                break;
            }
        }
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //此方法在onMeasure-->onLayout-->onDraw()之后调用
        // 含义是对当前View的所有子View进行绘制,如果没有就不需要绘制
        if (bln_is_headerViewVisible) {
            drawChild(canvas, view_header, getDrawingTime());
        }
    }
}
