package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.TabList

/**
 * author jingting
 * date : 2020/12/3下午4:48
 */

class GetTabListRequest : Request() {


    override fun url(): String {
        return GifFun.BASE_WAN_URL + "project/tree/json"
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(TabList::class.java)
    }

}