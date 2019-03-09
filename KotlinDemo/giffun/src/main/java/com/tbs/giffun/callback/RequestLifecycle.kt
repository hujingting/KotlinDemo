package com.tbs.giffun.callback

interface RequestLifecycle {

    fun startLoading()

    fun loadFinished()

    fun loadFaied(msg: String?)
}