package com.tbs.kotlindemo.fragment

import android.os.Bundle
import com.tbs.kotlindemo.R
import com.tbs.kotlindemo.base.BaseFragment

class FindFragment: BaseFragment() {

    private var title: String? = null


    /**
     * 静态方法
     */
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

    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find
    }

    fun forTest() {
        val items = listOf("apple", "banana", "kiwifruit")
        for (item in items) {
            println(item)
        }
    }

    fun describe(obj: Any):String =
            when (obj) {
                1 -> "One"
                "hello" -> "greeting"
                else -> "unknow"
            }



}