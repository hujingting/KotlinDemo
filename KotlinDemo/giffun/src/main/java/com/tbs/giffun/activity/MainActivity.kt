package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.quxianggif.core.Const
import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.dp2px
import com.quxianggif.core.extension.logDebug
import com.quxianggif.core.extension.toBitmap
import com.quxianggif.core.util.GlobalUtil
import com.quxianggif.core.util.SharedUtil
import com.tbs.giffun.R
import com.tbs.giffun.event.MessageEvent
import com.tbs.giffun.event.ModifyUserInfoEvent
import com.tbs.giffun.fragment.FollowingFeedsFragment
import com.tbs.giffun.fragment.HotFeedsFragment
import com.tbs.giffun.fragment.WorldFeedsFragment
import com.tbs.giffun.utils.ColorUtils
import com.tbs.giffun.utils.UserUtil
import com.tbs.giffun.utils.gilde.CustomUrl
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import org.litepal.LitePalDB

class MainActivity : BaseActivity() {

    private lateinit var pagerAdapter: MyAdapter

    private lateinit var nicknameMe: TextView

    private lateinit var descriptionMe: TextView

    private lateinit var avatarMe: ImageView

    private lateinit var editImage: ImageView

    private var backPressTime = 0L

    private var currentPagerPosition = 0

    internal var isNeedToRefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIsNeedToRefresh()
        initDatabase()
        setContentView(R.layout.activity_main)
    }

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
            logDebug(TAG, "text area top $top , bottom $bottom , left $left , right $right")
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(left, top, right, bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
                    .generate { palette ->
                        val isDark = ColorUtils.isBitmapDark(palette, bitmap)
                        val color: Int
                        color = if (isDark) {
                            ContextCompat.getColor(this@MainActivity, R.color.white_text)
                        } else {
                            ContextCompat.getColor(this@MainActivity, R.color.primary_text)
                        }
                        nicknameMe.setTextColor(color)
                        descriptionMe.setTextColor(color)
                        editImage.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
            return false
        }
    }

    private fun initDatabase() {
        val litepalDB = LitePalDB.fromDefault("giffun_" + GifFun.getUserId().toString())
        LitePal.use(litepalDB)
    }

    override fun setupViews() {
        setupToolbar()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        setUpViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)

//        tabs.addOnTabSelectedListener()
    }

    private fun checkIsNeedToRefresh() {
        val autoRefresh = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(GlobalUtil.getString(R.string.key_auto_refresh), true)
        if (autoRefresh) {
            val lastUseTime = SharedUtil.read(Const.Feed.MAIN_LAST_USE_TIME, 0L)
            val timeNotUsed = System.currentTimeMillis() - lastUseTime
            logDebug(TAG, "not used for " + timeNotUsed / 1000 + " seconds")
            if (timeNotUsed > 10 * 60 * 1000) { // 超过10分钟未使用
                isNeedToRefresh = true
            }
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


    private fun setUpViewPager(viewPager: ViewPager) {
        pagerAdapter = MyAdapter(supportFragmentManager)
        pagerAdapter.addFragment(WorldFeedsFragment(), getString(R.string.world))
        pagerAdapter.addFragment(FollowingFeedsFragment(), getString(R.string.follow))
        pagerAdapter.addFragment(HotFeedsFragment(), getString(R.string.hot))
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        currentPagerPosition = SharedUtil.read(Const.Feed.MAIN_PAGER_POSITION, 0)
        if (currentPagerPosition < 0 || currentPagerPosition >= pagerAdapter.count) {
            currentPagerPosition = 0
        }
        viewPager.currentItem = currentPagerPosition
        viewPager.addOnPageChangeListener (object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPagerPosition = position

            }

            override fun onPageScrollStateChanged(state: Int) {

            }



        })
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
            val headView = navView.getHeaderView(0)
            val userLayout = headView.findViewById<LinearLayout>(R.id.userLayout)
            val descriptionLayout = headView.findViewById<LinearLayout>(R.id.descriptionLayout)
            val navHeaderBg = headView.findViewById<ImageView>(R.id.navHeaderBgImage)
            avatarMe = headView.findViewById(R.id.avatarMe)
            nicknameMe = headView.findViewById(R.id.nicknameMe)
            descriptionMe = headView.findViewById(R.id.descriptionMe)
            editImage = headView.findViewById(R.id.editImage)

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
                            .bitmapTransform(BlurTransformation(activity))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(navHeaderBgLoadListener)
                            .into(navHeaderBg)
                }
            } else {
                val bgImageWidth = navView.width
                val bgImageHeight = dp2px((250 + 25).toFloat()) /* 25为补偿系统状态栏高度，不加这个高度值图片顶部会出现状态栏的底色 */
                Glide.with(this)
                        .load(bgImage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(bgImageWidth, bgImageHeight)
                        .listener(navHeaderBgLoadListener)
                        .into(navHeaderBg)
            }

            userLayout.setOnClickListener {  }
            descriptionLayout.setOnClickListener {  }
        }
    }


    internal class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragmentTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitles[position]
        }
    }

    private val tabSelectedListener by lazy {

    }

    companion object {
        private const val TAG = "MainActivity"

        private const val REQUEST_SEARCH = 10000

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
