package com.quxianggif.core.model

/**
 * author jingting
 * date : 2021/4/15下午3:15
 */
class ColorList(override val modelId: Long) : Model() {

    var data : MutableList<ChinaColor> = arrayListOf()
}