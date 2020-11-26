package com.quxianggif.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quxianggif.R
import com.quxianggif.user.adapter.BannerAdapter
import kotlinx.android.synthetic.main.fragment_main_view.*

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

        adapter = activity?.let { BannerAdapter(it) }
        banner.setAdapter(adapter)
    }

}