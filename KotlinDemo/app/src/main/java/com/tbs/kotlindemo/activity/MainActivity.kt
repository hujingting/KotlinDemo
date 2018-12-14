package com.tbs.kotlindemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.tbs.kotlindemo.R
import com.tbs.kotlindemo.base.BaseActivity
import com.tbs.kotlindemo.fragment.FindFragment
import com.tbs.kotlindemo.fragment.HomeFragment
import com.tbs.kotlindemo.fragment.MineFragment
import com.tbs.kotlindemo.model.bean.TabEntity
import com.tbs.kotlindemo.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BaseActivity() {

    val mTitles = arrayOf("每日精选", "发现", "我的")
    val mUnSelectedIcons = intArrayOf(R.drawable.ic_home_normal, R.drawable.ic_discovery_normal, R.drawable.ic_mine_normal)
    val mSelectedIcons = intArrayOf(R.drawable.ic_home_selected, R.drawable.ic_discovery_selected, R.drawable.ic_mine_selected)

    private val mTabEntities = ArrayList<CustomTabEntity>()


    private var homeFragment: HomeFragment? = null
    private var findFragment: FindFragment? = null
    private var mineFragment: MineFragment? = null

    private var index = 0


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
//                切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 -> homeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                homeFragment = it
                transaction.add(R.id.fl_container, it, "home")
            }

            1 -> findFragment?.let {
                transaction.show(it)
            } ?: FindFragment.getInstance(mTitles[position]).let {
                findFragment = it
                transaction.add(R.id.fl_container, it, "find")
            }

            2->mineFragment?.let {
                transaction.show(it)
            }?:MineFragment.getInstance(mTitles[position]).let {
                mineFragment =it
                transaction.add(R.id.fl_container,it, "mine")
            }

            else ->{

            }
        }

        index = position
        tab_layout.currentTab = index
        transaction.commitAllowingStateLoss()
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        homeFragment?.let { transaction.hide(it) }
        findFragment?.let { transaction.hide(it) }
        mineFragment?.let { transaction.hide(it) }
    }

    override fun request() {

    }


    override fun initData() {

    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        if (tab_layout != null) {
            outState.putInt("currTabIndex", index)
        }
    }


    private var exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(exitTime) <= 2000) {
                finish()
            } else{
                exitTime = System.currentTimeMillis()
                showToast("再按一次退出程序")
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
