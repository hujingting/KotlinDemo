package com.tbs.giffun.callback

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

abstract class InfiniteScrollListener(private val layoutManager: RecyclerView.LayoutManager): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

        if (dy < 0 || isDataLoading() || isNoMoreData()) return

        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = layoutManager.itemCount
        var firstVisibleItem: Int? = null
        if (layoutManager is LinearLayoutManager) {
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager){
            val item = layoutManager.findFirstVisibleItemPositions(null)
            firstVisibleItem = item.min()
        }

        if (firstVisibleItem != null) {
            if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
                onLoadMore()
            }
        }

    }


    abstract fun onLoadMore()

    abstract fun isDataLoading(): Boolean

    abstract fun isNoMoreData(): Boolean

    companion object {
        private const val TAG = "InfiniteScrollListener"

        private const val VISIBLE_THRESHOLD = 1
    }
}