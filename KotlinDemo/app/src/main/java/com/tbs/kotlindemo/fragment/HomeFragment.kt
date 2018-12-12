package com.tbs.kotlindemo.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.tbs.kotlindemo.R
import com.tbs.kotlindemo.base.BaseFragment
import com.tbs.kotlindemo.presenter.HomePresenter

class HomeFragment: BaseFragment() {

    private val mPresenter by lazy { HomePresenter() }

    private var mTitle: String? = null

    private var num: Int = 1

//    private var mHomeAdapter:

    private var loadingMore  = false

    private var isRefresh = false

    private var title: String? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.title = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }



    override fun lazyLoad() {
        
    }

    override fun initView() {
        
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }
}