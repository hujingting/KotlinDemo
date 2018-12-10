package com.tbs.kotlindemo.base

interface IPresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()
}