package com.tbs.kotlindemo.fragment

import android.os.Bundle
import com.tbs.kotlindemo.base.BaseFragment

class HomeFragment: BaseFragment() {


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

    override fun lazyLoad() {

    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}