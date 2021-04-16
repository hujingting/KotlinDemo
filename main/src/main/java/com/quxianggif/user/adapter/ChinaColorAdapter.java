package com.quxianggif.user.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.quxianggif.R;
import com.quxianggif.core.model.ChinaColor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * author jingting
 * date : 2021/4/15下午4:47
 */
public class ChinaColorAdapter extends BaseQuickAdapter<ChinaColor, ChinaColorAdapter.ColorViewHolder> implements LoadMoreModule {


    public ChinaColorAdapter(int layoutResId, @Nullable List<ChinaColor> data) {
        super(layoutResId, data);
    }

    public ChinaColorAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull ColorViewHolder colorViewHolder, ChinaColor chinaColor) {
        String colorString = chinaColor.getColor();

        if (!TextUtils.isEmpty(colorString)) {
            colorViewHolder.ivColorItem.setBackgroundColor(android.graphics.Color.parseColor(colorString));
            colorViewHolder.tvColorItem.setText(chinaColor.getName());
        }
    }

    static class ColorViewHolder extends BaseViewHolder {

        ImageView ivColorItem;
        TextView tvColorItem;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivColorItem = itemView.findViewById(R.id.iv_color_item);
            tvColorItem = itemView.findViewById(R.id.tv_color_item);
        }

    }

}
