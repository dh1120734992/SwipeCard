package com.example.swipecard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by teitsuyoshi on 2018/6/27.
 */

public class SwipeCardAdapter extends RecyclerView.Adapter<SwipeCardAdapter.CardViewHolder> {
    private Context mContext;
    private List<Integer> mBgs;
    private LayoutInflater mLayoutInflater;

    public SwipeCardAdapter(Context context, List<Integer> bgs) {
        mContext = context;
        mBgs = bgs;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(mLayoutInflater.inflate(R.layout.item_card,parent,false));
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.mView.setBackgroundResource(mBgs.get(position));
    }

    @Override
    public int getItemCount() {
        return mBgs!=null?mBgs.size():0;
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        private TextView mView;

        public CardViewHolder(View itemView) {
            super(itemView);
            mView = (TextView) itemView.findViewById(R.id.view);

        }
    }

    public List<Integer> getDatas(){
        return mBgs;
    }
}
