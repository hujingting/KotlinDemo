package com.tbs.giffun.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.postDelayed
import com.tbs.giffun.R
import com.tbs.giffun.activity.MainActivity
import com.tbs.giffun.callback.InfiniteScrollListener
import com.tbs.giffun.callback.LoadDataListener
import com.tbs.giffun.callback.PendingRunnable
import com.tbs.giffun.event.CleanCacheEvent
import com.tbs.giffun.event.MessageEvent
import com.tbs.giffun.event.RefreshMainActivityFeedsEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 所有和feed相关Fragment的基类
 */
abstract class BaseFeedsFragment: BaseFragment() {

    /**
     * 判断是否正在加载更多Feeds。
     */
    internal var isLoadingMore = false

    lateinit var activity: MainActivity

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var recyclerView: RecyclerView

    internal lateinit var adapter: RecyclerView.Adapter<*>

    internal lateinit var loadDataListener: LoadDataListener

    internal lateinit var layoutManager: RecyclerView.LayoutManager

    var pendingRunnable = SparseArray<PendingRunnable>()

    var isLoadFailed: Boolean = false

    /**
     * 判断是否还有更多数据，当服务器端没有更多Feeds时，此值为true。
     */
    /**
     * 判断是否还有更多数据。
     * @return 当服务器端没有更多Feeds时，此值为true，否则此值为false。
     */
    var isNoMoreData = false
        internal set

    internal fun initViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadDataListener = this as LoadDataListener
        activity = getActivity() as MainActivity
        setUpRecyclerView()
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        recyclerView.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {

            override fun onLoadMore() {
                loadDataListener.onLoad()
            }

            override fun isDataLoading() = isLoadingMore

            override fun isNoMoreData() = isNoMoreData
        })

        swipeRefresh.setOnRefreshListener {
//            refreshLoads()
        }
        loadFeedsFromDB()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is RefreshMainActivityFeedsEvent) {
            if (isLoadFailed) { // 只要当加载失败的情况下，收到RefreshMainActivityFeedsEvent才会执行刷新，否则不进行刷新
                GifFun.getHandler().postDelayed(300) { // 略微进行延迟处理，使界面上可以看到波纹动画效果
                    reloadFeeds()
                }
            }
        } else if (messageEvent is CleanCacheEvent) {
            reloadFeeds()
        }
    }

    /**
     * 重新加载feeds，在加载过程中如果界面上没有元素则显示ProgressBar，如果界面上已经有元素则显示SwipeRefresh。
     */
    private fun reloadFeeds() {
        if (adapter.itemCount <= 1) {
            startLoading()
        } else {
            swipeRefresh.isRefreshing = true
        }
        refreshFeeds()
    }

    /**
     * 将RecyclerView滚动到顶部
     */
    fun scrollToTop() {
        if (adapter.itemCount != 0) {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    /**
     * 执行潜在的Pending任务。
     */
    fun executePendingRunnableList() {
        val size = pendingRunnable.size()
        if (size > 0) {
            for (i in 0 until size) {
                val index = pendingRunnable.keyAt(i)
                val runnable = pendingRunnable.get(index)
                runnable.run(index)
            }
            pendingRunnable.clear()
        }
    }


    internal abstract fun setUpRecyclerView()

    internal abstract fun loadFeeds(lastFeed: Long)

    internal abstract fun refreshFeeds()

    internal abstract fun loadFeedsFromDB()

    internal abstract fun dataSetSize(): Int

    companion object {
        private const val TAG = "BaseFeedsFragment"
    }

}