package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetProjectList

/**
 * author jingting
 * date : 2020/12/4下午2:50
 */
class GetProjectRequest : Request() {

    private var page : Int = 1
    private var id : Int = 0

    override fun url(): String {
        return GifFun.BASE_WAN_URL + "project/list/" + "$page/json?cid=$id"
    }

    fun page(page: Int) : GetProjectRequest {
        this.page = page
        return this
    }

    fun id(id: Int) : GetProjectRequest {
        this.id = id
        return this
    }

    override fun method(): Int {
        return GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(GetProjectList::class.java)
    }
}