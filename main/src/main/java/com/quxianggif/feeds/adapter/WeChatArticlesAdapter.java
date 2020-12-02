package com.quxianggif.feeds.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.quxianggif.R;
import com.quxianggif.core.model.Articles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.List;

/**
 * author jingting
 * date : 2020-08-0714:50
 */
public class WeChatArticlesAdapter extends BaseQuickAdapter<Articles, WeChatArticlesAdapter.WeChatArticleViewHolder> implements LoadMoreModule {


    public WeChatArticlesAdapter(int layoutResId, @Nullable List<Articles> data) {
        super(layoutResId, data);
    }

    public WeChatArticlesAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NotNull WeChatArticleViewHolder holder, Articles articles) {
        holder.tvWechatTime.setText(articles.getNiceShareDate());

        if (holder.getAdapterPosition() < 4) {
            holder.tvTopTag.setVisibility(View.VISIBLE);
        } else {
            holder.tvTopTag.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(articles.getAuthor())) {
            holder.tvWeChatAuthor.setText(articles.getAuthor());
        } else if (!TextUtils.isEmpty(articles.getShareUser())){
            holder.tvWeChatAuthor.setText(articles.getShareUser());
        }
        holder.tvWeChatTitle.setText(articles.getTitle());

    }


    static class WeChatArticleViewHolder extends BaseViewHolder {

        TextView tvWeChatAuthor;
        TextView tvWeChatTitle;
        TextView tvWechatTime;
        TextView tvTopTag;

        public WeChatArticleViewHolder(@NotNull View view) {
            super(view);
            tvWeChatAuthor = view.findViewById(R.id.tv_wechat_article_author);
            tvWeChatTitle = view.findViewById(R.id.tv_wechat_article_title);
            tvWechatTime = view.findViewById(R.id.tv_wechat_article_time);
            tvTopTag = view.findViewById(R.id.tv_top_tag);
        }
    }

}
