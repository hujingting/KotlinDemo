package com.quxianggif.common.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quxianggif.R
import com.quxianggif.network.model.BannerModel
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetWanMain
import com.quxianggif.network.model.Response
import com.quxianggif.network.request.GetBannerRequest
import com.quxianggif.user.adapter.BannerAdapter
import com.quxianggif.util.ResponseHandler
import com.quxianggif.util.ScreenUtils
import kotlinx.android.synthetic.main.fragment_main_view.*
import kotlinx.android.synthetic.main.item_banner_list.*

/**
 * author jingting
 * date : 2020/11/25下午3:33
 */
class MainFragment : BaseFragment() {


    private var adapter: BannerAdapter? = null;

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_main_view, container, false));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItemWith(banner)
        adapter = activity?.let { BannerAdapter(it) }

        BannerModel.getResponse(object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val bannerModel = response as BannerModel
                    val wanUsers = bannerModel.data
                    adapter?.data = wanUsers
                }
            }

            override fun onFailure(e: Exception) {

            }
        })

    }

    fun setItemWith(itemView: View) {
        val params = itemView.layoutParams
        params.width = ScreenUtils.screenWidth
        params.height = params.width * 718 / 1146
        itemView.layoutParams = params
    }

}