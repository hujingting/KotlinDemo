package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName

/**
 * author jingting
 * date : 2020-05-2914:24
 */
class WeChatArticlesMain : Model() {

    override val modelId: Long
        get() = id

    @SerializedName("id")
    var id : Long = 0

    @SerializedName("datas")
    var weChatArticles : List<WeChatArticles> = ArrayList()

    @SerializedName("curPage")
    var curPage : Int = 0

    @SerializedName("offset")
    var offset : Int = 0

    @SerializedName("over")
    var over : Boolean = false

    @SerializedName("pageCount")
    var pageCount : Int = 0;

    @SerializedName("size")
    var size : Int = 0

    @SerializedName("total")
    var total : Int = 0

}