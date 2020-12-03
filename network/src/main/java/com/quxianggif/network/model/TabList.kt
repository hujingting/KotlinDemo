package com.quxianggif.network.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.core.model.Articles
import com.quxianggif.core.model.Tab
import com.quxianggif.network.request.GetTabListRequest

/**
 * author jingting
 * date : 2020/12/3下午5:09
 */
class TabList : Response() {

    @SerializedName("data")
    var tabs : List<Tab> = ArrayList()

    companion object {
        fun getResponse(callback: Callback) {
            GetTabListRequest()
                    .setListener(callback)
        }
    }
}