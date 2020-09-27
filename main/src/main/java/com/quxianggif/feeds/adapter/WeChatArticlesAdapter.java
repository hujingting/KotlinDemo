package com.quxianggif.feeds.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.quxianggif.R;
import com.quxianggif.core.model.WeChatArticles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author jingting
 * date : 2020-08-0714:50
 */
public class WeChatArticlesAdapter extends BaseQuickAdapter<WeChatArticles, WeChatArticlesAdapter.WeChatArticleViewHolder> implements LoadMoreModule {


    public WeChatArticlesAdapter(int layoutResId, @Nullable List<WeChatArticles> data) {
        super(layoutResId, data);
    }

    public WeChatArticlesAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NotNull WeChatArticleViewHolder holder, WeChatArticles weChatArticles) {
        holder.tvWechatTime.setText(weChatArticles.getNiceShareDate());
        holder.tvWeChatAuthor.setText(weChatArticles.getAuthor());
        holder.tvWeChatTitle.setText(weChatArticles.getTitle());


    }


    static class WeChatArticleViewHolder extends BaseViewHolder {

        TextView tvWeChatAuthor;
        TextView tvWeChatTitle;
        TextView tvWechatTime;

        public WeChatArticleViewHolder(@NotNull View view) {
            super(view);
            tvWeChatAuthor = view.findViewById(R.id.tv_wechat_article_author);
            tvWeChatTitle = view.findViewById(R.id.tv_wechat_article_title);
            tvWechatTime = view.findViewById(R.id.tv_wechat_article_time);
        }
    }

}
