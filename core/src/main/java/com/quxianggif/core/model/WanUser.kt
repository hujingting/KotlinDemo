package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName

/**
 * author jingting
 * date : 2020-05-2914:24
 */
class WanUser : Model() {

    override val modelId: Long
        get() = id

    @SerializedName("id")
    var id : Long = 0

    var name : String = ""

    var userControlSetTop = false
}