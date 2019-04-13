package com.tbs.giffun.fragment

import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.logWarn
import com.quxianggif.core.extension.searchModelIndex
import com.quxianggif.core.extension.showToast
import com.quxianggif.core.model.WorldFeed
import com.quxianggif.core.util.GlobalUtil
import com.quxianggif.network.model.FetchWorldFeeds
import com.quxianggif.network.model.OriginThreadCallback
import com.quxianggif.network.model.Response
import com.tbs.giffun.R
import com.tbs.giffun.adapter.WorldFeedAdapter
import com.tbs.giffun.callback.PendingRunnable
import com.tbs.giffun.event.DeleteFeedEvent
import com.tbs.giffun.event.LikeFeedEvent
import com.tbs.giffun.event.MessageEvent
import com.tbs.giffun.event.ModifyUserInfoEvent
import com.tbs.giffun.utils.ResponseHandler
import com.tbs.giffun.view.SpaceItemDecoration
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findAll

class WorldFeedsFragment : WorldFallFeedsFragment() {

    /**
     * RecyclerView的数据源，用于存储所有展示中的Feeds
     */
    internal var feedList: MutableList<WorldFeed> = java.util.ArrayList()


    override fun setUpRecyclerView() {
        super.setUpRecyclerView()
        adapter = WorldFeedAdapter(this, feedList, imageWidth, layoutManager)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(adapter))
    }

    /**
     * 加载feeds。如果数据库有缓存则优先显示缓存内存，如果没有缓存则从网络获取feeds。
     */
    override fun loadFeeds(lastFeed: Long) {
        val isRefreshing = lastFeed <= 0
        FetchWorldFeeds.getResponse(lastFeed, object : OriginThreadCallback {
            override fun onResponse(response: Response) {
                handleFetchedFeeds(response, isRefreshing)
                isLoadingMore = false
            }

            override fun onFailure(e: Exception) {
                logWarn(TAG, e.message, e)
                if (isRefreshing) {
                    ResponseHandler.handleFailure(e)
                }
                activity.runOnUiThread {
                    loadFailed(null)
                    isLoadingMore = false
                }
            }

        })
    }

    override fun onLoad() {
        if (!isLoadingMore && feedList.isNotEmpty()) {
            isLoadingMore = true
            isLoadFailed = false
            isLoadingMore = true
            val lastFeed = feedList[feedList.size - 1].feedId
            loadFeeds(lastFeed)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is DeleteFeedEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                feedList.removeAt(index)
                adapter.notifyItemRemoved(index)
            }
        } else if (messageEvent is LikeFeedEvent) {
            if (messageEvent.from == LikeFeedEvent.FROM_WORLD) {
                return
            }
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                // 对于Feed点赞状态同步要延迟执行，等到ViewPager切换到相应的界面时再执行，否则会出现状态同步的问题
                val runnable = object : PendingRunnable {
                    override fun run(index: Int) {
                        val feed = feedList[index]
                        feed.isLikedAlready = messageEvent.type == LikeFeedEvent.LIKE_FEED
                        feed.likesCount = messageEvent.likesCount
                        adapter.notifyItemChanged(index)
                    }
                }
                pendingRunnable.put(index, runnable)
            }
        } else if (messageEvent is ModifyUserInfoEvent) {
            if (messageEvent.modifyNickname || messageEvent.modifyAvatar) {
                swipeRefresh.isRefreshing = true
                refreshFeeds()
            }
        } else {
            super.onMessageEvent(messageEvent)
        }
    }

    /**
     *
     * 处理获取世界频道feeds请求的返回结果。
     *
     * @param response
     * 服务器响应的获取feeds请求的实体类。
     * @param isRefreshing
     * true表示刷新请求，false表示加载更多请求。
     */
    private fun handleFetchedFeeds(response: Response, isRefreshing: Boolean) {
        isNoMoreData = false
        if (!ResponseHandler.handleResponse(response)) {
            val fetchWorldFeeds = response as FetchWorldFeeds
            val status = fetchWorldFeeds.status
            if (status == 0) {
                val feeds = fetchWorldFeeds.feeds
                if (isRefreshing) {
                    LitePal.deleteAll<WorldFeed>()
                    LitePal.saveAll(feeds)
                    activity.runOnUiThread {
                        feedList.clear()
                        feedList.addAll(feeds)
                        adapter.notifyDataSetChanged()
                        recyclerView.scrollToPosition(0)
                        loadFinished()
                    }
                } else {
                    val oldFeedsCount = feedList.size
                    LitePal.saveAll(feeds)
                    activity.runOnUiThread {
                        feedList.addAll(feeds)
                        recyclerView.stopScroll()
                        adapter.notifyItemRangeInserted(oldFeedsCount, feeds.size)
                        loadFinished()
                    }
                }
            } else if (status == 10004) {
                isNoMoreData = true
                activity.runOnUiThread {
                    adapter.notifyItemChanged(adapter.itemCount - 1)
                    loadFinished()
                }
            } else {
                logWarn(TAG, "Fetch feeds failed. ${GlobalUtil.getResponseClue(status, fetchWorldFeeds.msg)}")
                activity.runOnUiThread {
                    showToast(GlobalUtil.getString(R.string.fetch_data_failed))
                    loadFailed(GlobalUtil.getString(R.string.fetch_data_failed) + ": " + response.status)
                }
            }
        } else {
            activity.runOnUiThread { loadFailed(GlobalUtil.getString(R.string.unknown_error) + ": " + response.status) }
        }
    }

    override fun refreshFeeds() {
        loadFeeds(0)
    }

    override fun loadFeedsFromDB() {
        Thread(Runnable {
            val feeds = LitePal.findAll<WorldFeed>()
            if (feeds.isEmpty()) {
                refreshFeeds()
            } else {
                activity.runOnUiThread {
                    feedList.clear()
                    feedList.addAll(feeds)
                    adapter.notifyDataSetChanged()
                    loadFinished()
                }

                if (activity.isNeedToRefresh) {
                    isLoadingMore = true // 此处将isLoadingMore设为true，防止因为内容不满一屏自动触发加载更多事件，从而让刷新进度条提前消失
                    activity.runOnUiThread {
                        swipeRefresh.isRefreshing = true
                    }

                    GifFun.getHandler().postDelayed({ refreshFeeds() }, 1000)
                }
            }
        }).start()
    }

    override fun dataSetSize(): Int {
        return feedList.size
    }

    companion object {

        private const val TAG = "WorldFeedsFragment"
    }
}