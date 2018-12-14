package com.tbs.kotlindemo.presenter

import com.tbs.kotlindemo.base.BasePresenter
import com.tbs.kotlindemo.bean.HomeBean
import com.tbs.kotlindemo.contract.HomeContract
import com.tbs.kotlindemo.iview.IHomeView

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private var bannerHomeBean: HomeBean?= null

    private var nextPageUrl: String? = null



    override fun requestHomeData(num: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMoreData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}