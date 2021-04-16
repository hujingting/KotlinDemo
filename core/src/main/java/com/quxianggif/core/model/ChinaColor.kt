package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.core.R

/**
 * author jingting
 * date : 2021/4/15下午3:08
 */



class ChinaColor(override val modelId: Long) : Model() {

    var name : String = ""

    var color: String = ""

    var img: Int = 0

}