package com.quxianggif.common.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.quxianggif.R
import com.quxianggif.adapter.OnItemClickListerner
import com.quxianggif.core.model.WanUser
import com.quxianggif.feeds.ui.WeChatArticlesActivity
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetWanMain
import com.quxianggif.network.model.Response
import com.quxianggif.user.adapter.WanMainAdapter
import com.quxianggif.util.ResponseHandler
import kotlinx.android.synthetic.main.activity_wan_main.*
import kotlinx.android.synthetic.main.fragment_articles_view.*

/**
 * author jingting
 * date : 2020/11/25下午3:33
 */
class ArticlesFragment : BaseFragment() {

    private var adapter: WanMainAdapter? = null

    companion object {
        fun newInstance(): ArticlesFragment{

            return ArticlesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_articles_view, container, false));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        loadingWanMainData()

        adapter?.setOnItemClickListener(OnItemClickListerner() { which, obj ->
            val wanUser = obj as WanUser

            WeChatArticlesActivity.start(activity as BaseActivity, wanUser.id.toInt())
        })
    }

    private fun setRecyclerView() {
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rv_articles.layoutManager = manager
        adapter = activity?.let { WanMainAdapter(it) }
        rv_articles.adapter = adapter
    }

    private fun loadingWanMainData() {
        startLoading()
        GetWanMain.getResponse(object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val getWanMain = response as GetWanMain
                    var wanUsers = getWanMain.users
                    for (wanUser in wanUsers) {
                        val name = wanUser.name
                        if (TextUtils.equals(name, "奇卓社") || TextUtils.equals(name, "GcsSloop") || TextUtils.equals(name, "互联网侦察")
                                || TextUtils.equals(name, "susion随心") || TextUtils.equals(name, "Gityuan")) {
                            wanUsers = wanUsers - wanUser
                        }
                    }
                    adapter?.data = wanUsers
                }
            }

            override fun onFailure(e: Exception) {

            }
        })
    }
}