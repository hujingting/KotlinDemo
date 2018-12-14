package com.tbs.kotlindemo.fragment

import android.os.Bundle
import android.view.View
import com.tbs.kotlindemo.R
import com.tbs.kotlindemo.base.BaseFragment
import com.tbs.kotlindemo.showToast
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() , View.OnClickListener{

    private var title: String? = null

    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.title = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun onClick(view: View?) {
        when {
            view?.id == R.id.iv_avatar || view?.id == R.id.tv_view_homepage ->{
                TODO()
            }

            view?.id == R.id.iv_about ->{
                TODO()
            }

            view?.id == R.id.tv_mine_attention->showToast("关注")
        }
    }


    override fun initView() {

        tv_view_homepage.setOnClickListener(this)
        iv_avatar.setOnClickListener(this)
        iv_about.setOnClickListener(this)

        tv_collection.setOnClickListener(this)
        tv_comment.setOnClickListener(this)

        tv_mine_message.setOnClickListener(this)
        tv_mine_attention.setOnClickListener(this)
        tv_mine_cache.setOnClickListener(this)
        tv_watch_history.setOnClickListener(this)
        tv_feedback.setOnClickListener(this)
    }

    override fun lazyLoad() {

    }


}