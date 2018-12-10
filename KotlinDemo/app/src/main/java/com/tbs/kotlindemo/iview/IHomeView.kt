package com.tbs.kotlindemo.iview

import com.tbs.kotlindemo.base.IBaseView
import com.tbs.kotlindemo.bean.HomeBean

interface IHomeView : IBaseView{

    fun setHomeData()

    fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>)

    fun showError(msg: String, errorCode: Int)

}