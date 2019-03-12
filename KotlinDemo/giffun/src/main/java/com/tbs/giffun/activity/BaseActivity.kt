package com.tbs.giffun.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import com.quxianggif.core.extension.logWarn
import com.quxianggif.core.util.AndroidVersion
import com.tbs.giffun.R
import com.tbs.giffun.callback.PermissionListener
import com.tbs.giffun.callback.RequestLifecycle
import com.tbs.giffun.event.ForceToLoginEvent
import com.tbs.giffun.event.MessageEvent
import com.tbs.giffun.utils.ActivityCollector
import kotlinx.android.synthetic.main.loading.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * 所有activity的基类
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), RequestLifecycle {

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

    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions
     * 要检查和处理的运行时权限数组
     * @param listener
     * 用于接收授权结果的监听器
     */
    protected fun handlePermission(permissions: ArrayList<String>?, listener: PermissionListener) {
        if (permissions == null || activity == null) {
            return
        }

        val requestPermissionList = ArrayList<String>()
        this.listener = listener

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission)
            }
        }

        if (!requestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity!!, requestPermissionList.toTypedArray(), 1)
        } else {
            listener.onGranted()
        }
    }


    /**
     * 隐藏软键盘。
     */
    fun hideSoftKeyboard() {
        try {
            val view = currentFocus
            if (view != null) {
                val binder = view.windowToken
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            logWarn(TAG, e.message, e)
        }
    }

    /**
     * 显示软键盘。
     */
    fun showSoftKeyboard(editText: EditText?) {
        try {
            if (editText != null) {
                editText.requestFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(editText, 0)
            }
        } catch (e: Exception) {
            logWarn(TAG, e.message, e)
        }
    }

    /**
     * 当Activity中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showLoadErrorView(tips: String) {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }

        val viewStub = findViewById<ViewStub>(R.id.loadErrorView)
        if (viewStub != null) {
            loadErrorView = viewStub.inflate()
            val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tips
        }
    }

    /**
     * 当Activity中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调
     */
    protected fun showBadNetworkView(listener: View.OnClickListener) {
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.badNetworkView)
        if (viewStub != null) {
            badNetworkView = viewStub.inflate()
            val badNetworkRootView = badNetworkView?.findViewById<View>(R.id.badNetworkRootView)
            badNetworkRootView?.setOnClickListener(listener)
        }
    }

    /**
     * 当Activity中没有任何内容的时候，通过此方法显示提示界面给用户。
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String) {
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.noContentView)
        if (viewStub != null) {
            noContentView = viewStub.inflate()
            val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
        }
    }

    /**
     * 将load error view进行隐藏。
     */
    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    /**
     * 将no content view进行隐藏。
     */
    protected fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    /**
     * 将bad network view进行隐藏。
     */
    protected fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    fun showProgressDialog(title: String?, message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this).apply {
                if (title != null) {
                    setTitle(title)
                }
                setMessage(message)
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    fun closeProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is ForceToLoginEvent) {
            if (isActive) { // 判断Activity是否在前台，防止非前台的Activity也处理这个事件，造成打开多个LoginActivity的问题。
                // force to login
                ActivityCollector.finishAll()
//                LoginActivity.actionStart(this, false, null)
            }
        }
    }

    open fun permissionGranted() {
        //由子类来具体实现
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    listener!!.onGranted()
                } else {
                    listener!!.onDenied(deniedPermissions)
                }
            }
            else -> {

            }
        }
    }

    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    override fun loadFaied(msg: String?) {
        loading?.visibility = View.GONE
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

}