package com.example.swipecard;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dh on 2018/6/27.
 */

public class SwipeCardLayoutManager extends RecyclerView.LayoutManager {
    //当前item最大显示数
    private int mMaxShowCount = 4;
    //向左右展示偏移
    private int mOffsetRight = 20;
    //item左下偏移
    private int mTransLB;
    //item缩放比例
    private float mScaleRate = 0.05f;

    private Context mContext;

    private RecyclerView mRecyclerView;

    private ItemTouchHelper mHelper;
    public SwipeCardLayoutManager(Context context, RecyclerView recyclerView, ItemTouchHelper helper) {
        mContext = context;
        mTransLB = DensityUtils.dp2px(context,15);
//        mOffsetRight = mTransLB*mMaxShowCount;
        mRecyclerView = recyclerView;

        mHelper = helper;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        //将所有的itemView先全部detach，放到Scrap集合里面，进行重新排布
        detachAndScrapAttachedViews(recycler);

        //获取当前recycler中的条目书
        int count = getItemCount();

        //取条目中前最大显示数的条目下标
        int mShowItemPos;
        if(count<=mMaxShowCount){
            mShowItemPos = 0;
        }else{
            mShowItemPos = count-mMaxShowCount;
        }

        //遍历排布
        for(int i = mShowItemPos;i<count;i++){
            //获取item 并添加到recycle中
            View item = recycler.getViewForPosition(i);
            addView(item);

            //测量item
            measureChildWithMargins(item,0,0);
            //确定摆放位置
            //获取除item之外的空余空间 + 偏移量 让整体卡片横向居中
            int left = (getWidth()-getDecoratedMeasuredWidth(item))/2+mOffsetRight;
            int top = (getHeight()-getDecoratedMeasuredHeight(item))/2;

            //重新摆放
            layoutDecorated(item,left,top,left+getDecoratedMeasuredWidth(item),top+getDecoratedMeasuredHeight(item));

            //当前item的层级 0 1 2 3
            int tier = count-i-1;
            //根据不同层级进行偏移和缩放
            item.setTranslationY(tier * mTransLB / 2); //向下偏移
            item.setTranslationX(-tier * mTransLB); //向左偏移
            //宽高缩放
            item.setScaleX(1-mScaleRate*tier);
            item.setScaleY(1-mScaleRate*tier);

            //当为最上层的item时获取拖动事件
            if(tier==0){
                item.setOnTouchListener(mOnTouchListener);
            }
        }


    }


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override

        public boolean onTouch(View v, MotionEvent event) {

            RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);

            // 把触摸事件交给 mItemTouchHelper，让其处理卡片滑动事件
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mHelper.startSwipe(childViewHolder);
            }

            return false;

        }

    };
}
