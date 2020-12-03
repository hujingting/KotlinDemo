package com.quxianggif.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * author jingting
 * date : 2020/12/3下午5:39
 */
class ProjectChildFragment : BaseFragment() {

    companion object {
        fun newInstance(id: Long): ProjectChildFragment{
            val childFragment = ProjectChildFragment()
            val args = Bundle()
            args.putInt("id", id.toInt())
            childFragment.arguments = args
            return childFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}