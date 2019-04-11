package com.tbs.giffun.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.quxianggif.core.model.WorldFeed
import com.tbs.giffun.fragment.WorldFeedsFragment

class WorldFeedAdapter(private val fragment: WorldFeedsFragment, feedList: List<WorldFeed>, imageWidth: Int,
                       layoutManager: RecyclerView.LayoutManager): WaterFallFeedAdapter<WorldFeed>(fragment.activity, feedList, imageWidth, layoutManager) {

    override var isNoMoreData: Boolean = false
        get() = fragment.isNoMoreData

    override var isLoadFailed: Boolean = false
        get() = fragment.isLoadFailed

    override fun onLoad() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindFeedHolder(holder: FeedViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createFeedHolder(parent: ViewGroup): FeedViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}