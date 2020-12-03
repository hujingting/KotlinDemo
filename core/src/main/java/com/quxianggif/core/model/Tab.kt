package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName

/**
 * author jingting
 * date : 2020/12/3下午5:13
 */
class Tab : Model() {

    override val modelId: Long
        get() = id

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("name")
    var name: String = ""
}