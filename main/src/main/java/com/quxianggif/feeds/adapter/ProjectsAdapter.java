package com.quxianggif.feeds.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.quxianggif.R;
import com.quxianggif.core.model.Articles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author jingting
 * date : 2020-08-0714:50
 */
public class ProjectsAdapter extends BaseQuickAdapter<Articles, ProjectsAdapter.WeChatArticleViewHolder> implements LoadMoreModule {


    public ProjectsAdapter(int layoutResId, @Nullable List<Articles> data) {
        super(layoutResId, data);
    }

    public ProjectsAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NotNull WeChatArticleViewHolder holder, Articles articles) {
        holder.tvTime.setText(articles.getNiceShareDate());

        if (!TextUtils.isEmpty(articles.getAuthor())) {
            holder.tvAuthor.setText(articles.getAuthor());
        } else if (!TextUtils.isEmpty(articles.getShareUser())){
            holder.tvAuthor.setText(articles.getShareUser());
        }

        holder.tvTitle.setText(articles.getTitle());
        holder.tvDesc.setText(articles.getDesc());

        Glide.with(getContext())
                .load(articles.getEnvelopePic())
                .placeholder(R.drawable.no_content_image)
                .error(R.drawable.no_content_image)
                .into(holder.ivImg);

    }


    static class WeChatArticleViewHolder extends BaseViewHolder {

        TextView tvAuthor;
        TextView tvTitle;
        TextView tvTime;
        TextView tvDesc;
        ImageView ivImg;

        public WeChatArticleViewHolder(@NotNull View view) {
            super(view);
            tvAuthor = view.findViewById(R.id.tv_project_author);
            tvTitle = view.findViewById(R.id.tv_project_title);
            tvTime = view.findViewById(R.id.tv_project_time);
            tvDesc = view.findViewById(R.id.tv_project_desc);
            ivImg = view.findViewById(R.id.iv_project_img);
        }
    }

}
