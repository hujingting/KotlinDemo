package com.quxianggif.user.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.bumptech.glide.Glide

import com.quxianggif.R
import com.quxianggif.adapter.BasicRecycleAdapter
import com.quxianggif.core.model.WanUser
import com.quxianggif.util.ScreenUtils

/**
 * author jingting
 * date : 2020-05-2911:04
 */
class WanMainAdapter(context: Context) : BasicRecycleAdapter<WanUser>(context) {

    var screenWith = ScreenUtils.screenWidth

    var headIconUrl = intArrayOf(R.drawable.img_hong_yang, R.drawable.img_guo_lin_blog, R.drawable.img_yu_gang_talk, R.drawable.img_chen_xiang_mo_ying,
            R.drawable.img_android_qun_ying_zhuan, R.drawable.img_code_xiao_sheng,
            R.drawable.img_google_developer, R.drawable.img_mei_yuan, R.drawable.img_yi_fei_yuan)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_wan_main_list, parent, false)
        return WanMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WanMainViewHolder

        val wanUser = getItem(position)
        viewHolder.wanUser.text = wanUser.name

        setItemWith(viewHolder.wanHeadIcon)

//        Glide.with(mContext)
//                .load(headIconUrl[position])
//                .placeholder(R.drawable.avatar_default)
//                .error(R.drawable.avatar_default)
//                .into(holder.wanHeadIcon)

        holder.wanHeadIcon.load(headIconUrl[position]) {
            placeholder(R.drawable.avatar_default)
            error(R.drawable.avatar_default)
        }

        viewHolder.itemView.setOnClickListener {
            if (listener != null) {
                listener.click(position, wanUser)
            }
        }
    }

    fun setItemWith(itemView: View) {
        val params = itemView.layoutParams
        params.width = (screenWith - ScreenUtils.dip2px(30f))/ 2
        params.height = params.width
        itemView.layoutParams = params
    }


    internal class WanMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var wanUser: TextView = itemView.findViewById(R.id.tv_wan_user)
        var wanHeadIcon: ImageView = itemView.findViewById(R.id.iv_wan_head_icon)

    }
}
