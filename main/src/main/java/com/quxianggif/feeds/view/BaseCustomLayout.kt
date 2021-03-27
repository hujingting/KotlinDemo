package com.quxianggif.feeds.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.quxianggif.R

/**
 * author jingting
 * date : 2021/3/24下午4:08
 */
abstract class BaseCustomLayout(context: Context) : ViewGroup(context) {

    protected val Int.dp: Int get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    protected fun View.autoMeasure() {
        measure(
                this.defaultWidthMeasureSpec(this@BaseCustomLayout),
                this.defaultHeightMeasureSpec(this@BaseCustomLayout)
        )
    }

    protected fun View.defaultWidthMeasureSpec(viewGroup: ViewGroup): Int {
        return when (layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> viewGroup.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(viewGroup: ViewGroup): Int {
        return when (layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> viewGroup.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected class LayoutParams(width: Int, height: Int) : MarginLayoutParams(width, height)

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

    protected fun View.layout(x:Int, y: Int, fromRight: Boolean = false) {
        if (!fromRight) {
            layout(x, y, x + measuredWidth, y + measuredHeight);
        } else {
            layout(this@BaseCustomLayout.measuredWidth - x - measuredWidth, y)
        }
    }
}