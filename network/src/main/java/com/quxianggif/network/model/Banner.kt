package com.quxianggif.network.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.core.model.Model

/**
 * author jingting
 * date : 2020/11/27上午11:16
 */
class Banner : Model() {

    override val modelId: Long
        get() = id.toLong()

//    @SerializedName("id")
    var id : Int = 0

//    @SerializedName("desc")
    var desc : String = ""

//    @SerializedName("imagePath")
    var imagePath : String = ""

//    @SerializedName("isVisible")
    var isVisible : Int = 0

//    @SerializedName("title")
    var title : String = ""

//    @SerializedName("type")
    var type : Int = 0

//    @SerializedName("url")
    var url : String = ""

}