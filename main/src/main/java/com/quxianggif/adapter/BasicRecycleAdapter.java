package com.quxianggif.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.umeng.commonsdk.statistics.common.MLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LiuXianJun on 2019/7/30
 */

public abstract class BasicRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context mContext;
    protected List<T> data;
    protected OnItemClickListerner listener;

    public BasicRecycleAdapter(Context context) {
        this.mContext = context;
    }

    public List<T> getData() {
        return data;
    }

    public void  setData(List<T> data) {
        this.data = data;
        MLog.d(">>>>数据: " + data.toString());
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (getData() != null) {
            this.data.addAll(data);
            MLog.d(">>>>添加数据: " + data.toString());
            notifyDataSetChanged();
        } else {
            setData(data);
        }
    }

    public void addData(T data) {
        if (getData() != null) {
            this.data.add(data);
            MLog.d(">>>>添加数据: " + data.toString());
            notifyDataSetChanged();
        } else {
            List<T> dataList = new ArrayList<>();
            dataList.add(data);
            setData(dataList);
        }
    }

    public void delete(int position) {
        getData().remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.data != null ? this.data.size() : 0;
    }

    protected T getItem(int position) {
        return this.data.get(position);
    }

    public void setOnItemClickListener(OnItemClickListerner listener) {
        this.listener = listener;
    }
}
