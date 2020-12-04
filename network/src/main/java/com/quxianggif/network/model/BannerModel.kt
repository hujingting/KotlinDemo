package com.quxianggif.network.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.network.request.GetBannerRequest

/**
 * author jingting
 * date : 2020/11/27上午11:02
 */
class BannerModel : Response() {

    @SerializedName("data")
    var data : List<Banner> = ArrayList()


    companion object {
        fun getResponse(callback: Callback) {
            GetBannerRequest()
                    .listen(callback)
        }
    }

}