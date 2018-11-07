package com.tbs.kotlindemo

import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.tbs.kotlindemo.model.TabEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity() {

    val mTitles = arrayOf("每日精选", "发现", "我的")
    val mUnSelectedIcons = intArrayOf(R.drawable.ic_home_normal, R.drawable.ic_discovery_normal, R.drawable.ic_mine_normal)
    val mSelectedIcons = intArrayOf(R.drawable.ic_home_selected, R.drawable.ic_discovery_selected, R.drawable.ic_mine_selected)

    private val mTabEntities = ArrayList<CustomTabEntity>()


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initTab()
    }

    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mSelectedIcons[it], mUnSelectedIcons[it]) }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
//                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    override fun request() {
    }

    override fun initData() {

    }
}
