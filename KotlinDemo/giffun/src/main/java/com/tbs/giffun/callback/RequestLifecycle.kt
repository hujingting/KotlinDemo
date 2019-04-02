package com.tbs.giffun.callback

interface RequestLifecycle {

    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)
}