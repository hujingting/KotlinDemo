package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.BannerModel
import com.quxianggif.network.model.Callback

/**
 * author jingting
 * date : 2020/11/27上午10:52
 */
class GetBannerRequest : Request() {

    override fun url(): String {
        return GifFun.BASE_WAN_URL + "banner/json"
    }

    override fun method(): Int {
        return GET
    }

//    override fun params(): Map<String, String>? {
//        val params = HashMap<String, String>()
//        return if (buildAuthParams(params)) {
//            params
//        } else super.params()
//    }
//
//    override fun headers(builder: Headers.Builder): Headers.Builder {
//        buildAuthHeaders(builder, NetworkConst.DEVICE_SERIAL, NetworkConst.TOKEN)
//        return super.headers(builder)
//    }


    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(BannerModel::class.java)
    }

}