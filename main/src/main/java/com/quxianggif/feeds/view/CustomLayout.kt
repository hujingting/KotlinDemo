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
class CustomLayout(context: Context) : ViewGroup(context) {

    protected val Int.dp:Int get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP;
        setImageResource(R.drawable.img_android_qun_ying_zhuan)
        layoutParams = LayoutParams(MATCH_PARENT, 280.dp)

        addView(this)
    }

    val fab = FloatingActionButton(context).apply {
        setImageResource(R.drawable.follow_button)
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            (it as MarginLayoutParams).marginEnd = 12.dp
            it.marginEnd = 12.dp
        }

        addView(this)
    }


    val title = AppCompatTextView(context).apply {
        setText("test")
        setTextColor(Color.YELLOW)
        setTextSize(16f)
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        header.autoMeasure()
        fab.autoMeasure()
        title.autoMeasure()
    }

    protected fun View.autoMeasure() {
        measure(
                this.defaultWidthMeasureSpec(this@CustomLayout),
                this.defaultHeightMeasureSpec(this@CustomLayout)
        )
    }


    protected fun View.defaultWidthMeasureSpec(viewGroup: ViewGroup): Int {
        return when(layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> viewGroup.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else-> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(viewGroup: ViewGroup): Int {
        return when(layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> viewGroup.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else-> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected class LayoutParams(width:Int, height:Int) : MarginLayoutParams(width, height)

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}