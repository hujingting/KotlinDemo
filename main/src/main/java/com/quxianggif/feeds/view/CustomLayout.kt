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
public class CustomLayout(context: Context) : BaseCustomLayout(context) {

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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        header.layout(0,0)
    }
}