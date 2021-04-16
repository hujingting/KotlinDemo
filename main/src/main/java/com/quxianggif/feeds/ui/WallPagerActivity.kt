package com.quxianggif.feeds.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.quxianggif.R
import com.quxianggif.common.ui.BaseActivity
import com.quxianggif.core.model.Color
import com.quxianggif.core.model.ColorList
import com.quxianggif.user.adapter.ColorAdapter
import com.quxianggif.util.GetJsonUtil
import kotlinx.android.synthetic.main.activity_wall_pager.*

/**
 * author jingting
 * date : 2021/4/14下午5:02
 */
class WallPagerActivity : BaseActivity() {

    var adapter : ColorAdapter? = null;
    var colorList: ColorList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wall_pager)

        val colorString = GetJsonUtil.getJsonString(this, "color.json") ?: return

        colorList = GetJsonUtil.jsonToObject(colorString, ColorList::class.java)

        if (colorList != null) {
            setRecyclerView()
        }
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_colors.layoutManager = manager
        adapter = ColorAdapter(R.layout.item_color_list, colorList?.data)
        adapter!!.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        adapter!!.loadMoreModule.isEnableLoadMore = false
        // 是否自动加载下一页（默认为true）
        adapter!!.loadMoreModule.isAutoLoadMore = false

        //预加载的位置（默认为1）
//        adapter.loadMoreModule.preLoadNumber = 3

//        adapter!!.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
//            page += 1
//            loadData(page)
//        })

//        adapter?.addHeaderView(view)
        rv_colors.adapter = adapter

        adapter!!.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val colorItem = adapter.getItem(position) as Color
            iv_wallpaper.setBackgroundColor(android.graphics.Color.parseColor(colorItem.color))
        })

    }


    companion object {

        private const val TAG = "WallPagerActivity"

        public const val REQUEST_SEARCH = 10000

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, WallPagerActivity::class.java)
            activity.startActivity(intent)
        }
    }


}