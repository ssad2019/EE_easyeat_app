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
    private boolean mHeaderViewVisible;
    private View mHeaderView;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private PinnedListViewAdapter mAdapter;
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
        mHeaderView = headerView;
        mAdapter = (PinnedListViewAdapter) getAdapter();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            //测量HeaderView的宽高
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            //宽
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            //高
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //摆放HeaderView
        if (mHeaderView != null) {
            //layout摆放的位置（左，上，右，下）
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            //根据listview第一个条目的postion,确定HeaderView隐藏/显示/上移
            configureHeaderView(getFirstVisiblePosition());
        }
    }
    public void configureHeaderView(int position) {
        if (mHeaderView == null || null == mAdapter) {
            return;
        }
        int state = mAdapter.getPinnedHeaderState(position);
        switch (state) {
            case PinnedListViewAdapter.PINNED_HEADER_GONE: {
                //如果是不可见的状态，就不绘制HeaderView
                mHeaderViewVisible = false;
                break;
            }
            case PinnedListViewAdapter.PINNED_HEADER_VISIBLE: {
                //如果当前是可见的状态，设置HeaderView的内容
                mAdapter.configurePinnedHeader(mHeaderView, position);
                //将被隐藏的HeaderView(在界面外绘制)摆放到顶部
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                //绘制HeaderView
                mHeaderViewVisible = true;
                break;
            }
            case PinnedListViewAdapter.PINNED_HEADER_PUSHED_UP: {
                //获取第一个view
                View firstView = getChildAt(0);
                //返回View自身底边到父布局顶边的距离
                int bottom = firstView.getBottom();
                //HeaderView的高度
                int headerHeight = mHeaderView.getHeight();
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                } else {
                    y = 0;
                }
                //绘制HeaderView的内容
                mAdapter.configurePinnedHeader(mHeaderView, position);
                //摆放HeaderView
                if (mHeaderView.getTop() != y) {
                    //HeaderView上移y
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //此方法在onMeasure-->onLayout-->onDraw()之后调用
        // 含义是对当前View的所有子View进行绘制,如果没有就不需要绘制
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}
