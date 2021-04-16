package com.quxianggif.feeds.ui

import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.quxianggif.R
import com.quxianggif.common.ui.BaseActivity
import com.quxianggif.core.model.ChinaColor
import com.quxianggif.core.model.ColorList
import com.quxianggif.user.adapter.ChinaColorAdapter
import com.quxianggif.user.adapter.SmartisanWallpaperAdapter
import com.quxianggif.util.GetJsonUtil
import com.quxianggif.util.ScreenUtils
import kotlinx.android.synthetic.main.activity_wall_pager.*

/**
 * author jingting
 * date : 2021/4/14下午5:02
 */
class WallPagerActivity : BaseActivity() {

    var adapterChina : ChinaColorAdapter? = null;
    var smartisanWallPaperAdapter : SmartisanWallpaperAdapter? = null
    var colorList: ColorList? = null
    var current_color: String = "#4e7ca1"
    var wallPaperColorList: ColorList? = null
    var current_img: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wall_pager)

        val colorString = GetJsonUtil.getJsonString(this, "color.json") ?: return

        /**
         * 故宫颜色
         */
        colorList = GetJsonUtil.jsonToObject(colorString, ColorList::class.java)

        if (colorList != null) {
            setRecyclerView()
        }

        /**
         * 锤子壁纸
         */
        val wallPaperString = GetJsonUtil.getJsonString(this, "smartisan_wallpaper.json")
        wallPaperColorList = GetJsonUtil.jsonToObject(wallPaperString, ColorList::class.java)

        if (wallPaperColorList != null) {
            setWallPaperRecyclerView()
        }


        tv_wallpaper_setting.setOnClickListener(View.OnClickListener {

            var bitmap: Bitmap? = null;

            if (current_img != 0) {
                 bitmap = BitmapFactory.decodeResource(resources, current_img)
            } else {
                bitmap = Bitmap.createBitmap(ScreenUtils.screenWidth, ScreenUtils.screenHeight, Bitmap.Config.ARGB_8888)
            }

            if (current_img == 0) {
                val canvas = Canvas(bitmap)
                canvas.drawColor(android.graphics.Color.parseColor(current_color))
            }

            val wallpaperManager = WallpaperManager.getInstance(baseContext)
            wallpaperManager.setBitmap(bitmap)
            Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * 设置锤子壁纸
     */
    private fun setWallPaperRecyclerView() {
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_wall_papers.layoutManager = manager
        smartisanWallPaperAdapter = SmartisanWallpaperAdapter(R.layout.item_color_list, wallPaperColorList?.data)
        smartisanWallPaperAdapter!!.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        smartisanWallPaperAdapter!!.loadMoreModule.isEnableLoadMore = false
        // 是否自动加载下一页（默认为true）
        smartisanWallPaperAdapter!!.loadMoreModule.isAutoLoadMore = false

        //预加载的位置（默认为1）
//        adapterChina.loadMoreModule.preLoadNumber = 3

//        adapterChina!!.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
//            page += 1
//            loadData(page)
//        })

//        adapterChina?.addHeaderView(view)
        rv_wall_papers.adapter = smartisanWallPaperAdapter

        smartisanWallPaperAdapter!!.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val colorItem = adapter.getItem(position) as ChinaColor
            current_img = smartisanWallPaperAdapter!!.getDrawableRes(colorItem.img)
            if (current_img == 0) {
                return@OnItemClickListener
            }
            iv_wallpaper.setImageResource(current_img)
        })
    }

    /**
     *
     * 设置故宫颜色
     */
    private fun setRecyclerView() {
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_colors.layoutManager = manager
        adapterChina = ChinaColorAdapter(R.layout.item_color_list, colorList?.data)
        adapterChina!!.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        adapterChina!!.loadMoreModule.isEnableLoadMore = false
        // 是否自动加载下一页（默认为true）
        adapterChina!!.loadMoreModule.isAutoLoadMore = false

        //预加载的位置（默认为1）
//        adapterChina.loadMoreModule.preLoadNumber = 3

//        adapterChina!!.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
//            page += 1
//            loadData(page)
//        })

//        adapterChina?.addHeaderView(view)
        rv_colors.adapter = adapterChina

        adapterChina!!.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val colorItem = adapter.getItem(position) as ChinaColor
            current_color = colorItem.color
            iv_wallpaper.setImageResource(0)
            iv_wallpaper.setBackgroundColor(android.graphics.Color.parseColor(colorItem.color))
            current_img = 0
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