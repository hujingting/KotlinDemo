package com.quxianggif.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create by LiuXianJun on 2019/7/30
 */

public abstract class BasicRecycleListAdapter<BEAN> extends BasicRecycleAdapter<BEAN> {
    private int layoutRes;
    private Class<? extends RecyclerView.ViewHolder> viewHolderClazz;

    public BasicRecycleListAdapter(Context context, int layoutRes, Class<? extends RecyclerView.ViewHolder> viewHolderClazz) {
        super(context);
        this.layoutRes = layoutRes;
        this.viewHolderClazz = viewHolderClazz;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(layoutRes, viewGroup, false);
        return getViewHolder(view);
    }

    private RecyclerView.ViewHolder getViewHolder(View view) {
        try {
            return viewHolderClazz.getConstructor(View.class).newInstance(view);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        bindData(position, viewHolder, getItem(position));
    }

    abstract void bindData(int position, RecyclerView.ViewHolder viewHolder, BEAN obj);
}
