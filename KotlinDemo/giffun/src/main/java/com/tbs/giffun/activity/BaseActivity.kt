package com.tbs.giffun.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toolbar
import com.tbs.giffun.callback.RequestLifecycle
import java.lang.ref.WeakReference

/**
 * 所有activity的基类
 */

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), RequestLifecycle{

    protected var isActive: Boolean = false

    protected var activity: Activity? = null

    protected var loading: ProgressBar? = null

    private var loadErrorView: View? = null

    private var badNetworkView: View? = null

    private var noContentView: View? = null

    private var weakRefActivity: WeakReference<Activity>? = null

    protected var toolbar: Toolbar? = null

    protected var progressDialog: ProgressDialog? = null

    private var

    override fun startLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFinished() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFaied(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}