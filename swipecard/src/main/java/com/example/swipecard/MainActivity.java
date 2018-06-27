package com.example.swipecard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private RecyclerView mSwipeCardList;

    private List<Integer> mBgs = new ArrayList<>();
    private SwipeCardAdapter mSwipeCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBgs.add(R.drawable.shape_swipe_card_1);
        mBgs.add(R.drawable.shape_swipe_card_2);
        mBgs.add(R.drawable.shape_swipe_card_3);
        mBgs.add(R.drawable.shape_swipe_card_4);
        mBgs.add(R.drawable.shape_swipe_card_5);
        mSwipeCardList = (RecyclerView) findViewById(R.id.swipe_card_list);
//        mSwipeCardList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback cardCallBack = new SwipeCardCallBack(this);
        ItemTouchHelper helper = new ItemTouchHelper(cardCallBack);
        helper.attachToRecyclerView(mSwipeCardList);
        mSwipeCardList.setLayoutManager(new SwipeCardLayoutManager(this,mSwipeCardList,helper));
        mSwipeCardAdapter = new SwipeCardAdapter(this, mBgs);
        mSwipeCardList.setAdapter(mSwipeCardAdapter);

    }
}
