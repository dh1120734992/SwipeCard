package com.example.swipecard;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

/**
 * Created by dh on 2018/6/27.
 */

public class SwipeCardCallBack extends ItemTouchHelper.SimpleCallback {
    private RecyclerView mRecyclerView;
    private List<Integer> mDatas;
    //当前item最大显示数
    private int mMaxShowCount = 4;

    //item左下偏移
    private int mTransLB;

    //item缩放比例
    private float mScaleRate = 0.05f;

    public SwipeCardCallBack(Context context) {
        super(0, 0);
        mTransLB = DensityUtils.dp2px(context,15);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = 0;
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof SwipeCardLayoutManager) {
            //设置滑动的方向
            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ;

        }

        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //移除并添加数据实现循环
        SwipeCardAdapter swipeCardAdapter = (SwipeCardAdapter) mRecyclerView.getAdapter();
        mDatas = swipeCardAdapter.getDatas();
        int bg = mDatas.remove(viewHolder.getLayoutPosition());
        mDatas.add(0,bg);
        swipeCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //最大移动距离
        double maxMove = Math.hypot(recyclerView.getWidth()/2,recyclerView.getHeight()/2);
        //当前移动距离
        double currentMove = Math.hypot(dX,dY);

        //当前动画的进度
        double p = currentMove/maxMove>1?1:currentMove/maxMove;
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            //执行
            View item = recyclerView.getChildAt(i);
            int tier = count - i - 1;
            if (tier > 0) {
                if (tier < mMaxShowCount) {
                    //将其他层级的item进行向中偏移并放大
                    item.setTranslationX((float) (1 - mTransLB * tier + p * mTransLB));
                    item.setTranslationY((float) -(1 - mTransLB * tier + p * mTransLB)/2);
                    item.setScaleX((float) (1 - mScaleRate * tier + p * mScaleRate));
                    item.setScaleY((float) (1 - mScaleRate * tier + p * mScaleRate));
                }
            }else{
                if(dX!=0){
                    //根据item的移动方向的不同进行不同的角度反转
                    item.setRotation((float) (p * 45*dX/Math.abs(dX)));
                }
            }
        }

    }


    //防止除了最顶上的item滑动外，其他层级的item也滑动，需要在LayoutManager中重新设置顶层item的滑动监听
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    //旋转后由于条目复用会使下一层的item也进行旋转
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置角度
        viewHolder.itemView.setRotation(0f);
    }
}
