package com.quxianggif.feeds.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.navigation.NavigationView
import com.quxianggif.R
import com.quxianggif.common.ui.*
import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.*
import com.quxianggif.core.util.GlobalUtil
import com.quxianggif.event.MessageEvent
import com.quxianggif.event.ModifyUserInfoEvent
import com.quxianggif.settings.ui.SettingsActivity
import com.quxianggif.user.ui.ModifyUserInfoActivity
import com.quxianggif.user.ui.RecommendFollowingActivity
import com.quxianggif.user.ui.UserHomePageActivity
import com.quxianggif.util.ColorUtils
import com.quxianggif.util.UserUtil
import com.quxianggif.util.glide.CustomUrl
import com.skydoves.androidbottombar.BottomMenuItem
import com.skydoves.androidbottombar.OnMenuItemSelectedListener
import com.skydoves.androidbottombar.animations.BadgeAnimation
import com.skydoves.androidbottombar.forms.badgeForm
import com.skydoves.androidbottombar.forms.iconForm
import com.skydoves.androidbottombar.forms.titleForm
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_wan_main.*
import kotlinx.android.synthetic.main.activity_wan_main.navView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author jingting
 * date : 2020-05-2716:45
 */
class WanMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private lateinit var nicknameMe: TextView

    private lateinit var descriptionMe: TextView

    private lateinit var avatarMe: ImageView

    private lateinit var editImage: ImageView

    private var mainFragment: MainFragment? = null
    private var articlesFragment: ArticlesFragment? = null;
    private var projectFragment: ProjectFragment? = null;

    //默认为0
    private var mIndex = -1

    private var navHeaderBgLoadListener: RequestListener<Any, GlideDrawable> = object : RequestListener<Any, GlideDrawable> {
        override fun onException(e: Exception?, model: Any, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(glideDrawable: GlideDrawable?, model: Any, target: Target<GlideDrawable>,
                                     isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
            if (glideDrawable == null) {
                return false
            }
            val bitmap = glideDrawable.toBitmap()
            val bitmapWidth = bitmap.width
            val bitmapHeight = bitmap.height
            if (bitmapWidth <= 0 || bitmapHeight <= 0) {
                return false
            }
            val left = (bitmapWidth * 0.2).toInt()
            val right = bitmapWidth - left
            val top = bitmapHeight / 2
            val bottom = bitmapHeight - 1
            logDebug(WanMainActivity.TAG, "text area top $top , bottom $bottom , left $left , right $right")
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(left, top, right, bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .generate { palette ->
                        val isDark = ColorUtils.isBitmapDark(palette, bitmap)
                        val color: Int
                        color = if (isDark) {
                            ContextCompat.getColor(this@WanMainActivity, R.color.white_text)
                        } else {
                            ContextCompat.getColor(this@WanMainActivity, R.color.primary_text)
                        }
                        nicknameMe.setTextColor(color)
                        descriptionMe.setTextColor(color)
                        editImage.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wan_main)

        if (mIndex == -1) {
            switchFragment(0)
        } else {
            switchFragment(mIndex)
        }

        setBottomBarView();

    }

    private fun setBottomBarView() {

        val titleForm = titleForm(this) {
            setTitleColor(Color.WHITE)
            setTitleActiveColorRes(R.color.white)
        }

        val iconForm = iconForm(this) {
            setIconSize(28)
        }

        bottomBarView.addBottomMenuItems(
                listOf(
                        BottomMenuItem(this)
                                .setTitleForm(titleForm)
                                .setIconForm(iconForm)
                                .setTitle("发现")
                                .setIcon(R.drawable.icon_find_tab)
                                .build(),

                        BottomMenuItem(this)
                                .setTitleForm(titleForm)
                                .setIconForm(iconForm)
                                .setTitle("公众号")
                                .setIcon(R.drawable.icon_articles_tab)
                                .build(),

                        BottomMenuItem(this)
                                .setTitleForm(titleForm)
                                .setIconForm(iconForm)
                                .setTitle("项目")
                                .setIcon(R.drawable.icon_project_tab)
                                .build()
                )
        )

        bottomBarView.onMenuItemSelectedListener = object : OnMenuItemSelectedListener {
            override fun onMenuItemSelected(index: Int, bottomMenuItem: BottomMenuItem, fromUser: Boolean) {
//                bottomBarView.dismissBadge(index)
                switchFragment(index)
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        showToast("onSaveInstanceState->"+mIndex)
//        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        outState.putInt("currTabIndex", mIndex)
    }

    override fun setupViews() {
        setupToolbar()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        navView.setNavigationItemSelectedListener(this)
        navView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                navView.viewTreeObserver.removeOnPreDrawListener(this)
                loadUserInfo()
                return false
            }
        })

        tv_main_tab.setOnClickListener(this)
        tv_project_tab.setOnClickListener(this)
        tv_wx_articles_tab.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.compose -> GifFun.getHandler().postDelayed(300) { PostFeedActivity.actionStart(this) }
            R.id.user_home -> GifFun.getHandler().postDelayed(300) {
                UserHomePageActivity.actionStart(this, avatarMe, GifFun.getUserId(),
                        UserUtil.nickname, UserUtil.avatar, UserUtil.bgImage)
            }
            R.id.draft -> GifFun.getHandler().postDelayed(300) { DraftActivity.actionStart(this) }
            R.id.recommend_following -> GifFun.getHandler().postDelayed(300) { RecommendFollowingActivity.actionStart(this) }
            R.id.settings -> GifFun.getHandler().postDelayed(300) { SettingsActivity.actionStart(this) }
            R.id.wan_android -> GifFun.getHandler().postDelayed(300) { MainActivity.actionStart(this) }
        }

        GifFun.getHandler().post {
            uncheckNavigationItems()
            drawerLayoutWan.closeDrawers()
        }
        return true
    }

    private fun uncheckNavigationItems() {
        navView.setCheckedItem(R.id.none)
    }

    /**
     * 加载登录用户的信息，头像和昵称等。
     */
    private fun loadUserInfo() {
        val count = navView.headerCount
        if (count == 1) {
            val nickname = UserUtil.nickname
            val avatar = UserUtil.avatar
            val description = UserUtil.description
            val bgImage = UserUtil.bgImage
            val headerView = navView.getHeaderView(0)
            val userLayout = headerView.findViewById<LinearLayout>(R.id.userLayout)
            val descriptionLayout = headerView.findViewById<LinearLayout>(R.id.descriptionLayout)
            val navHeaderBg = headerView.findViewById<ImageView>(R.id.navHeaderBgImage)
            avatarMe = headerView.findViewById(R.id.avatarMe)
            nicknameMe = headerView.findViewById(R.id.nicknameMe)
            descriptionMe = headerView.findViewById(R.id.descriptionMe)
            editImage = headerView.findViewById(R.id.editImage)

            nicknameMe.text = nickname
            if (TextUtils.isEmpty(description)) {
                descriptionMe.text = GlobalUtil.getString(R.string.edit_description)
            } else {
                descriptionMe.text = String.format(GlobalUtil.getString(R.string.description_content), description)
            }

            Glide.with(this)
                    .load(CustomUrl(avatar))
                    .bitmapTransform(CropCircleTransformation(activity))
                    .placeholder(R.drawable.loading_bg_circle)
                    .error(R.drawable.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(avatarMe)

            if (TextUtils.isEmpty(bgImage)) {
                if (!TextUtils.isEmpty(avatar)) {
                    Glide.with(this)
                            .load(CustomUrl(avatar))
                            .bitmapTransform(BlurTransformation(this, 15))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(navHeaderBgLoadListener)
                            .into(navHeaderBg)
                }
            } else {
                val bgImageWidth = navView.width
                val bgImageHeight = dp2px((250 + 25).toFloat() /* 25为补偿系统状态栏高度，不加这个高度值图片顶部会出现状态栏的底色 */)
                Glide.with(this)
                        .load(bgImage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(bgImageWidth, bgImageHeight)
                        .listener(navHeaderBgLoadListener)
                        .into(navHeaderBg)
            }
            userLayout.setOnClickListener { UserHomePageActivity.actionStart(this, avatarMe, GifFun.getUserId(), nickname, avatar, bgImage) }
            descriptionLayout.setOnClickListener { ModifyUserInfoActivity.actionEditDescription(this) }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is ModifyUserInfoEvent) {
            if (messageEvent.modifyAvatar || messageEvent.modifyBgImage || messageEvent.modifyDescription || messageEvent.modifyNickname) {
                loadUserInfo()
            }
        } else {
            super.onMessageEvent(messageEvent)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayoutWan.openDrawer(GravityCompat.START)
        }
        return true
    }

    private var backPressTime = 0L

    override fun onBackPressed() {
        if (drawerLayoutWan.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutWan.closeDrawer(GravityCompat.START)
        } else {
            val now = System.currentTimeMillis()
            if (now - backPressTime > 2000) {
                showToast(String.format(GlobalUtil.getString(R.string.press_again_to_exit), GlobalUtil.appName))
                backPressTime = now
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            tv_main_tab.id -> {
                switchFragment(0)
            }

            tv_wx_articles_tab.id -> {
                switchFragment(1)
            }

            tv_project_tab.id -> {
                switchFragment(2)
            }
        }
    }

    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mainFragment?.let { transaction.hide(it) }
        articlesFragment?.let { transaction.hide(it) }
        projectFragment?.let { transaction.hide(it) }
    }

    private fun switchFragment(pos: Int) {

        if (pos == mIndex) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when(pos) {
            //首页
            0 -> mainFragment?.let {
                transaction.show(it)
            } ?: MainFragment.newInstance().let {
                mainFragment = it
                transaction.add(R.id.fl_content, it, it.javaClass.simpleName)
            }

            //公众号
            1 -> articlesFragment?.let {
                transaction.show(it)
            } ?: ArticlesFragment.newInstance().let {
                articlesFragment = it
                transaction.add(R.id.fl_content, it, it.javaClass.simpleName)
            }

            //项目
            2 -> projectFragment?.let {
                transaction.show(it)
            } ?: ProjectFragment.newInstance().let {
                projectFragment = it
                transaction.add(R.id.fl_content, it, it.javaClass.simpleName)
            } else -> {
                
            }
        }

        mIndex = pos
        transaction.commitAllowingStateLoss()
    }

    companion object {

        private const val TAG = "WanMainActivity"

        fun start(activity: Activity) {
            val intent = Intent(activity, WanMainActivity::class.java);
            activity.startActivity(intent)
        }
    }

}