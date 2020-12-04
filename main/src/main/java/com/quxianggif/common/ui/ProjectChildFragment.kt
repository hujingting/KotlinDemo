package com.quxianggif.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.quxianggif.R
import com.quxianggif.core.model.Articles
import com.quxianggif.feeds.adapter.ProjectsAdapter
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetProjectList
import com.quxianggif.network.model.Response
import com.quxianggif.util.ResponseHandler
import kotlinx.android.synthetic.main.fragment_main_view.*

/**
 * author jingting
 * date : 2020/12/3下午5:39
 */
class ProjectChildFragment : BaseFragment() {

    var page: Int = 1;
    var adapter: ProjectsAdapter? = null;
    var id: Int? = 0;

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
        return inflater.inflate(R.layout.fragment_child_ui, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.isRefreshing = true
        setRecyclerView()

        swipeRefresh.setOnRefreshListener {
            page = 1
            loadData(page)
        }

        id = arguments?.getInt("id", 0);
        loadData(page)
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recycler_view.layoutManager = manager;
        adapter = ProjectsAdapter(R.layout.item_project_list)
        recycler_view.adapter = adapter;
        adapter!!.adapterAnimation = SlideInBottomAnimation()
        // 打开或关闭加载更多功能（默认为true）
        adapter!!.loadMoreModule.isEnableLoadMore = true
        // 是否自动加载下一页（默认为true）
        adapter!!.loadMoreModule.isAutoLoadMore = true
        //预加载的位置（默认为1）
//        adapter.loadMoreModule.preLoadNumber = 3
        adapter!!.loadMoreModule.setOnLoadMoreListener {
            page += 1
            loadData(page)
        }

        adapter!!.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val wechatArticles = adapter.getItem(position) as Articles
            activity?.let { WebViewActivity.actionStart(it, wechatArticles.title, wechatArticles.link) }
        })
    }

    private fun loadData(page: Int) {
        GetProjectList.getResponse(page, id!!, object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val getProject = response as GetProjectList;
                    val projectList = getProject.projectList;

                    if (page == 1) {
                        adapter?.setList(projectList.articles)
                        swipeRefresh?.isRefreshing = false
                    } else if (page >= projectList.total){
                        adapter?.loadMoreModule?.loadMoreEnd()
                    } else{
                        adapter?.addData(projectList.articles)
                        adapter?.loadMoreModule?.loadMoreComplete()
                    }
                }
            }

            override fun onFailure(e: Exception) {
                adapter?.loadMoreModule?.loadMoreFail()
                swipeRefresh.isRefreshing = false
            }
        })
    }
}