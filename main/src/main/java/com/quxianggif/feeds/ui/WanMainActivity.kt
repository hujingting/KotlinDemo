package com.quxianggif.feeds.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.navigation.NavigationView
import com.quxianggif.R
import com.quxianggif.adapter.OnItemClickListerner
import com.quxianggif.common.ui.BaseActivity
import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.*
import com.quxianggif.core.model.WanUser
import com.quxianggif.core.util.GlobalUtil
import com.quxianggif.event.MessageEvent
import com.quxianggif.event.ModifyUserInfoEvent
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetWanMain
import com.quxianggif.network.model.Response
import com.quxianggif.settings.ui.SettingsActivity
import com.quxianggif.user.adapter.WanMainAdapter
import com.quxianggif.user.ui.ModifyUserInfoActivity
import com.quxianggif.user.ui.RecommendFollowingActivity
import com.quxianggif.user.ui.UserHomePageActivity
import com.quxianggif.util.ColorUtils
import com.quxianggif.util.ResponseHandler
import com.quxianggif.util.UserUtil
import com.quxianggif.util.glide.CustomUrl
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
class WanMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {

    internal lateinit var adapter: WanMainAdapter

    private lateinit var nicknameMe: TextView

    private lateinit var descriptionMe: TextView

    private lateinit var avatarMe: ImageView

    private lateinit var editImage: ImageView

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wan_main)

        setRecyclerView()

        loadingWanMainData()

        adapter.setOnItemClickListener(OnItemClickListerner() { which, obj ->
             val wanUser = obj as WanUser

            WeChatArticlesActivity.start(this, wanUser.id.toInt())
        })
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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.compose -> GifFun.getHandler().postDelayed(300){ PostFeedActivity.actionStart(this) }
            R.id.user_home -> GifFun.getHandler().postDelayed(300) {
                UserHomePageActivity.actionStart(this, avatarMe, GifFun.getUserId(),
                        UserUtil.nickname, UserUtil.avatar, UserUtil.bgImage)
            }
            R.id.draft -> GifFun.getHandler().postDelayed(300) { DraftActivity.actionStart(this) }
            R.id.recommend_following -> GifFun.getHandler().postDelayed(300) { RecommendFollowingActivity.actionStart(this) }
            R.id.settings -> GifFun.getHandler().postDelayed(300) { SettingsActivity.actionStart(this) }
            R.id.wan_android -> GifFun.getHandler().postDelayed(300) {MainActivity.actionStart(this)}
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

    private fun loadingWanMainData() {
        startLoading()
        GetWanMain.getResponse(object : Callback {
            override fun onResponse(response: Response) {
                if (ResponseHandler.handleWanResponse(response)) {
                    val getWanMain = response as GetWanMain
                    var wanUsers  = getWanMain.users
                    for (wanUser in wanUsers) {
                        val name = wanUser.name
                        if (TextUtils.equals(name, "奇卓社") || TextUtils.equals(name, "GcsSloop") || TextUtils.equals(name, "互联网侦察")
                                || TextUtils.equals(name, "susion随心") || TextUtils.equals(name, "Gityuan")) {
                            wanUsers = wanUsers - wanUser
                        }
                    }
                    adapter.data = wanUsers
                }
            }

            override fun onFailure(e: Exception) {

            }
        })
    }

    private fun setRecyclerView() {
        val manager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rv_wan.layoutManager = manager
        adapter = WanMainAdapter(this)
        rv_wan.adapter = adapter
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


    companion object {

        private const val TAG = "WanMainActivity"

        fun start(activity: Activity) {
            val intent = Intent(activity, WanMainActivity::class.java);
            activity.startActivity(intent)
        }
    }
}