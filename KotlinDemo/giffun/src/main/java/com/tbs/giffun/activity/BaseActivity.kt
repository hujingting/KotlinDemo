package com.tbs.giffun.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toolbar
import com.quxianggif.core.util.AndroidVersion
import com.tbs.giffun.R
import com.tbs.giffun.callback.PermissionListener
import com.tbs.giffun.callback.RequestLifecycle
import com.tbs.giffun.utils.ActivityCollector
import kotlinx.android.synthetic.main.loading.view.*
import org.greenrobot.eventbus.EventBus
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

    private var listener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        weakRefActivity = WeakReference(this)
        ActivityCollector.add(weakRefActivity)
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        isActive = true
//        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        isActive = false
//        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakRefActivity)
        EventBus.getDefault().unregister(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        loading = findViewById(R.id.loading)
    }

    protected fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    protected fun transparentStatusBar() {
        if (AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    protected fun handle

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