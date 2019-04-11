package com.tbs.giffun.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.quxianggif.core.extension.dp2px
import com.tbs.giffun.R
import com.tbs.giffun.callback.LoadDataListener
import org.greenrobot.eventbus.EventBus

abstract class WorldFallFeedsFragment: BaseFeedsFragment(), LoadDataListener {

    /**
     * 通过获取屏幕宽度来计算出每张图片的宽度。
     *
     * @return 计算后得出的每张图片的宽度。
     */
    internal val imageWidth: Int
        get() {
            val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay?.getMetrics(metrics)
            val columnWidth = metrics.widthPixels / COLUMN_COUNT
            return columnWidth - dp2px(18f)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_water_fall_feeds, container, false)
        initViews(view)
        EventBus.getDefault().register(this)
        return super.onCreateView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun setUpRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    companion object {

        private const val TAG = "WaterFallFeedsFragment"

        private const val COLUMN_COUNT = 2
    }

}