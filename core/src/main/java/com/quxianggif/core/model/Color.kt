package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName

/**
 * author jingting
 * date : 2021/4/15下午3:08
 */



class Color(override val modelId: Long) : Model() {

    var name : String = ""

    var color: String = ""

}