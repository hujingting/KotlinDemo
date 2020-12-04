package com.quxianggif.feeds.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.quxianggif.common.ui.BaseActivity
import com.quxianggif.feeds.adapter.MainArticlesAdapter
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetWechatArticles
import com.quxianggif.network.model.Response
import com.quxianggif.util.ResponseHandler
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.quxianggif.R
import com.quxianggif.common.ui.WebViewActivity
import com.quxianggif.core.model.Articles
import com.quxianggif.core.util.AndroidVersion
import com.quxianggif.feeds.adapter.WeChatArticlesAdapter
import kotlinx.android.synthetic.main.activity_wechat_article.*


/**
 * author jingting
 * date : 2020-06-1816:05
 */
class WeChatArticlesActivity: BaseActivity() {

    internal lateinit var adapter: WeChatArticlesAdapter
    var page = 1
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wechat_article)


        id = intent.getIntExtra("id", 0);

        swipeRefresh.isRefreshing = true
        setRecyclerView()
        loadData(page)

        swipeRefresh.setOnRefreshListener {
            page = 1
            loadData(page)
        }
    }


    private fun loadData(page: Int) {
        GetWechatArticles.getResponse(id, page, object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val weChatAritcle = response as GetWechatArticles
                    val wechatArticlesMain = weChatAritcle.wechatArticelsMain
                    val wechatArticles = wechatArticlesMain.articles

                    if (page == 1) {
                        adapter.setList(wechatArticles)
                        swipeRefresh.isRefreshing = false
                    } else if (page >= wechatArticlesMain.total){
                       adapter.loadMoreModule.loadMoreEnd()
                    } else{
                        adapter.addData(wechatArticles)
                        adapter.loadMoreModule.loadMoreComplete()
                    }

                }
            }

            override fun onFailure(e: Exception) {
                adapter.loadMoreModule.loadMoreFail()
                swipeRefresh.isRefreshing = false
            }
        })
    }


    @SuppressLint("RestrictedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_search -> {
                if (AndroidVersion.hasLollipopMR1()) { // Android 5.0版本启用transition动画会存在一些效果上的异常，因此这里只在Android 5.1以上启用此动画
                    val searchMenuView: View? = toolbar?.findViewById(R.id.menu_search)
                    val options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                            getString(R.string.transition_search_back)).toBundle()
                    val intent = Intent(this, WanSearchActivity::class.java)
                    intent.putExtra("id", id)
                    startActivityForResult(intent, MainActivity.REQUEST_SEARCH, options)
                } else {
                    val intent = Intent(this, WanSearchActivity::class.java)
                    intent.putExtra("id", id)
                    startActivityForResult(intent, MainActivity.REQUEST_SEARCH)
                }
//                composeFab.visibility = View.GONE // 当进入搜索界面键盘弹出时，composeFab会随着键盘往上偏移。暂时没查到原因，使用隐藏的方式先进行规避
            }
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun setupViews() {
        setupToolbar()
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_wan.layoutManager = manager
        adapter = WeChatArticlesAdapter(com.quxianggif.R.layout.item_wechat_articles)
        adapter.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        adapter.loadMoreModule.isEnableLoadMore = true
        // 是否自动加载下一页（默认为true）
        adapter.loadMoreModule.isAutoLoadMore = true
        //预加载的位置（默认为1）
//        adapter.loadMoreModule.preLoadNumber = 3

        adapter.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
            page += 1
            loadData(page)
        })

        rv_wan.adapter = adapter


        adapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val wechatArticles = adapter.getItem(position) as Articles
            WebViewActivity.actionStart(this, wechatArticles.title, wechatArticles.link)
        })

    }


    companion object {
        private const val TAG = "WeChatArticleActivity"

        fun start(activity: BaseActivity, id: Int) {
            val intent = Intent(activity, WeChatArticlesActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }
}