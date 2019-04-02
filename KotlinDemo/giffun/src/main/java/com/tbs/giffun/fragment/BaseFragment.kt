package com.tbs.giffun.fragment

import android.support.v4.app.Fragment
import com.tbs.giffun.callback.RequestLifecycle

open class BaseFragment: Fragment(), RequestLifecycle {


    override fun startLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFinished() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFailed(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}