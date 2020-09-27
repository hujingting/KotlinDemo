package com.quxianggif.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

@SuppressLint("StaticFieldLeak")
object ScreenUtils {

    private var mScreenWidth = 0
    private var mScreenHeight = 0
    val statusBarHeight = 0
    private var mScale = 1f
    private var mFontScale = 1f
    private var mContext: Context? = null

    val screenWidth: Int
        get() {
            if (mScreenWidth == 0) {
                initScreen(mContext!!)
            }
            return mScreenWidth
        }

    val screenHeight: Int
        get() {
            if (mScreenHeight == 0) {
                initScreen(mContext!!)
            }

            return mScreenHeight
        }

    fun initScreen(context: Context) {

        mContext = context
        val displaymetrics = DisplayMetrics()
        val window = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        window.defaultDisplay.getMetrics(displaymetrics)
        mScreenWidth = displaymetrics.widthPixels
        mScreenHeight = displaymetrics.heightPixels

        //        Class<?> c = null;
        //        Object obj = null;
        //        Field field = null;
        //        int x = 0, sbar = 0;
        //        try {
        //            c = Class.forName("com.android.internal.R$dimen");
        //            obj = c.newInstance();
        //            field = c.getField("status_bar_height");
        //            x = Integer.parseInt(field.get(obj).toString());
        //            sbar = context.getResources().getDimensionPixelSize(x);
        //        } catch (Exception e1) {
        //            e1.printStackTrace();
        //        }
        //        mStatusBarHeight = sbar;

        mScale = context.resources.displayMetrics.density
        mFontScale = context.resources.displayMetrics.scaledDensity
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2dip(pxValue: Float): Int {
        if (mScreenWidth == 0) {
            initScreen(mContext!!)
        }

        return (pxValue / mScale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    fun dip2px(dipValue: Float): Int {

        if (mScreenWidth == 0) {
            initScreen(mContext!!)
        }

        return (dipValue * mScale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float): Int {

        if (mScreenWidth == 0) {
            initScreen(mContext!!)
        }

        return (pxValue / mFontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(spValue: Float): Int {
        if (mScreenWidth == 0) {
            initScreen(mContext!!)
        }

        return (spValue * mFontScale + 0.5f).toInt()
    }


}
