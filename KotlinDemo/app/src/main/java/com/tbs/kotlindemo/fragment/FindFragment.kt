package com.tbs.kotlindemo.fragment

import android.os.Bundle
import com.tbs.kotlindemo.base.BaseFragment

class FindFragment: BaseFragment() {

    private var title: String? = null

    companion object {
        fun getInstance(title: String): FindFragment {
            val fragment = FindFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.title = title
            return fragment
        }
    }

    override fun lazyLoad() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}