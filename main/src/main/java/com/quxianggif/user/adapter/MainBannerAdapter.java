package com.quxianggif.user.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.quxianggif.R;
import com.quxianggif.common.ui.WebViewActivity;
import com.quxianggif.core.GifFun;
import com.quxianggif.network.model.Banner;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import androidx.annotation.NonNull;

/**
 * author jingting
 * date : 2020/11/27下午5:15
 */
public class MainBannerAdapter extends BaseBannerAdapter<Banner, MainBannerAdapter.BannerViewHolder> {


    @Override
    protected void onBind(BannerViewHolder holder, Banner data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public BannerViewHolder createViewHolder(View itemView, int viewType) {
        return new BannerViewHolder(itemView);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner_list;
    }

    public static class BannerViewHolder extends BaseViewHolder<Banner> {

        ImageView ivBanner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBanner = findView(R.id.iv_banner);
        }

        @Override
        public void bindData(Banner data, int position, int pageSize) {
            Glide.with(GifFun.getContext())
                    .load(data.getImagePath())
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .into(ivBanner);

            ivBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebViewActivity.Companion.actionStart(GifFun.getContext(), data.getTitle(), data.getUrl());
                }
            });
        }
    }
}
