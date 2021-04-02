package com.quxianggif.network.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.network.request.GetBannerRequest

/**
 * author jingting
 * date : 2020/11/27上午11:02
 */
class BannerModel constructor(data: List<Banner>) : Response() {

    @SerializedName("data")
    var data : List<Banner> = ArrayList()

    //当一个类中同时有主构造器与次构造器的时候，需要这样写：
    // 可以看出 : 符号在 Kotlin 中非常高频出现，它其实表示了一种依赖关系，在这里表示依赖于主构造器。
    constructor(data: List<Banner>, id : Int) : this(data) {

    }

    companion object {
        fun getResponse(callback: Callback) {
            GetBannerRequest()
                    .listen(callback)
        }

        //和上面代码一样
//        fun getReponse(callback: Callback) = GetBannerRequest().listen(callback)

    }

}