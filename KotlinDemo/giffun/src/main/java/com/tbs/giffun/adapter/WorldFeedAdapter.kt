package com.tbs.giffun.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quxianggif.core.model.WorldFeed
import com.tbs.giffun.R
import com.tbs.giffun.fragment.WorldFeedsFragment

class WorldFeedAdapter(private val fragment: WorldFeedsFragment, feedList: List<WorldFeed>, imageWidth: Int,
                       layoutManager: RecyclerView.LayoutManager): WaterFallFeedAdapter<WorldFeed>(fragment.activity, feedList, imageWidth, layoutManager) {

    override var isNoMoreData: Boolean = false
        get() = fragment.isNoMoreData

    override var isLoadFailed: Boolean = false
        get() = fragment.isLoadFailed

    override fun onLoad() {
        fragment.onLoad()
    }

    override fun createFeedHolder(parent: ViewGroup): FeedViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.world_feed_item, parent, false)
        val holder = WorldFeedViewHolder(view)
        baseCreateFeedHolder(holder)
        return holder
    }

    override fun bindFeedHolder(holder: FeedViewHolder, position: Int) {
        val viewHolder = holder as WorldFeedViewHolder
        baseBindFeedHolder(viewHolder, position)
    }

    private class WorldFeedViewHolder internal constructor(view: View) : WaterFallFeedAdapter.FeedViewHolder(view)

    companion object {

        private const val TAG = "WorldFeedAdapter"
    }

}