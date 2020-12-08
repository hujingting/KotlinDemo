package com.quxianggif.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quxianggif.R
import com.quxianggif.core.model.Tab
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.Response
import com.quxianggif.network.model.TabList
import com.quxianggif.user.adapter.ProjectPagerAdapter
import com.quxianggif.util.ResponseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_project_view.*

/**
 * author jingting
 * date : 2020/11/25下午3:33
 */
class ProjectFragment : BaseFragment() {

    var fragments : MutableList<BaseFragment> = ArrayList()
    var pagerAdapter: ProjectPagerAdapter? = null

    companion object {
        fun newInstance(): ProjectFragment{
            return ProjectFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_project_view, container, false));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabList.getResponse(object : Callback {

            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val tabList = response as TabList
                    val tabs = tabList.tabs;

                    setViewPager(tabs)
                }
            }

            override fun onFailure(e: Exception) {

            }
        })
    }


    private fun setViewPager(tabs: List<Tab>) {

        for (tab in tabs) {
            tab_layout.addTab(tab_layout.newTab().setText(tab.name))
            fragments.add(ProjectChildFragment.newInstance(tab.id))
        }

        pagerAdapter = ProjectPagerAdapter(fragmentManager!!, tabs, fragments)
        view_pager.adapter = pagerAdapter
        view_pager.offscreenPageLimit = tabs.size
        tab_layout.setupWithViewPager(view_pager)
    }
}