package com.quxianggif.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.quxianggif.R
import com.quxianggif.common.ui.BaseFragment
import com.quxianggif.common.ui.WebViewActivity
import com.quxianggif.core.model.Articles
import com.quxianggif.ext.loadUrl
import com.quxianggif.feeds.adapter.MainArticlesAdapter
import com.quxianggif.network.model.*
import com.quxianggif.user.adapter.BannerAdapter
import com.quxianggif.util.ResponseHandler
import com.quxianggif.util.ScreenUtils
import com.zs.zs_jetpack.ui.main.home.HomeVM
import kotlinx.android.synthetic.main.fragment_main_view.*

/**
 * author jingting
 * date : 2020/11/25下午3:33
 */
class HomeFragment : BaseFragment(), BGABanner.Adapter<ImageView?, String?>, BGABanner.Delegate<ImageView?, String?> {

    var adapter: MainArticlesAdapter? = null
    var page = 0
    private var bannerAdapter: BannerAdapter? = null;
//    private var adapterChina: MainBannerAdapter? = null
    var homeVm: HomeVM? = null
    var bannerList: MutableList<Banner>? = null

    internal lateinit var view: View
    internal lateinit var banner: BGABanner
    var topArticleList: List<Articles> = ArrayList()


    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun observe() {
//        homeVm?.banner?.observe(this, Observer {
//            bannerList = it
//            initBanner()
//
//        })
    }

    private fun initBanner() {
        banner.apply {
            setAutoPlayAble(true)
            val views : MutableList<View> = ArrayList()
            bannerList?.forEach {
                views.add(ImageView(mActivity).apply {
                    setBackgroundResource(R.drawable.ripple_bg)
                })
            }

            setAdapter(this@HomeFragment)
            setDelegate(this@HomeFragment)
            setData(views)
        }
    }

    override fun fillBannerItem(banner: BGABanner?, itemView: ImageView?, model: String?, position: Int) {
        itemView?.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            bannerList?.get(position)?.imagePath?.let { loadUrl(mActivity, it) }
        }
    }

    override fun onBannerItemClick(banner: BGABanner?, itemView: ImageView?, model: String?, position: Int) {
//        nav().navigate(R.id.action_main_fragment_to_web_fragment, Bundle().apply {
//            bannerList?.get(position)?.let {
//                putString("loadUrl", it.url)
//                putString("title", it.title)
//                putInt("id", it.id)
//            }
//        })
    }

    override fun initViewModel() {
        homeVm = getActivityViewModel(HomeVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.head_view_banner, null)
        banner = view.findViewById<BGABanner>(R.id.banner)
        return super.onCreateView(inflater.inflate(R.layout.fragment_main_view, container, false));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.isRefreshing = true
        setRecyclerView()

        setItemWith(banner)
        bannerAdapter = activity?.let { BannerAdapter(it) }
//        banner.setAdapter(bannerAdapter)

        BannerModel.getResponse(object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val bannerModel = response as BannerModel
                    val wanUsers = bannerModel.data
                    bannerAdapter?.data = wanUsers
//                    banner.refreshData(wanuser)
                    bannerList = bannerModel.data
                    initBanner()
                }
            }

            override fun onFailure(e: Exception) {

            }
        })


        swipeRefresh.setOnRefreshListener {
            page = 0
            loadData(page)
        }


        loadTopData()

    }

    private fun loadTopData() {
        GetTopArticles.getResponse(object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val topArticles = response as GetTopArticles
                    topArticleList = topArticles.topArticles
                    loadData(page)
                }
            }

            override fun onFailure(e: Exception) {
            }
        })
    }


    private fun loadData(page: Int) {
        GetMainArticles.getResponse(page, object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val articles = response as GetMainArticles
                    val articlesMain = articles.wechatArticelsMain
                    val articleList = articlesMain.articles

                    if (page == 0) {
                        if (topArticleList.isNotEmpty() && !articleList.containsAll(topArticleList)) {
                            articleList.addAll(0, topArticleList)
                        }
                        adapter?.setList(articleList)
                        swipeRefresh.isRefreshing = false
                    } else if (page >= articlesMain.total){
                        adapter?.loadMoreModule?.loadMoreEnd()
                    } else{
                        adapter?.addData(articleList)
                        adapter?.loadMoreModule?.loadMoreComplete()
                    }

                }
            }

            override fun onFailure(e: Exception) {
                adapter?.loadMoreModule?.loadMoreFail()
                swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = manager
        adapter = MainArticlesAdapter(R.layout.item_wechat_articles)
        adapter!!.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        adapter!!.loadMoreModule.isEnableLoadMore = true
        // 是否自动加载下一页（默认为true）
        adapter!!.loadMoreModule.isAutoLoadMore = true
        //预加载的位置（默认为1）
//        adapterChina.loadMoreModule.preLoadNumber = 3

        adapter!!.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener {
            page += 1
            loadData(page)
        })

        adapter?.addHeaderView(view)
        recycler_view.adapter = adapter

        adapter!!.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val wechatArticles = adapter.getItem(position) as Articles
            activity?.let { WebViewActivity.actionStart(it, wechatArticles.title, wechatArticles.link) }
        })

    }

    fun setItemWith(itemView: View) {
        val params = itemView.layoutParams
        params.width = ScreenUtils.screenWidth
        params.height = params.width * 718 / 1146
        itemView.layoutParams = params
    }


}