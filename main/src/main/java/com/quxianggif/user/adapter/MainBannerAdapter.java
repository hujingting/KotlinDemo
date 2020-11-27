package com.quxianggif.user.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quxianggif.R;
import com.quxianggif.core.model.WanUser;
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
        BannerViewHolder holder1 = holder;
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

        }
    }
}
