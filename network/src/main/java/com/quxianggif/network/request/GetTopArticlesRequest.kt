package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetMainArticles
import com.quxianggif.network.model.GetTopArticles
import com.quxianggif.network.model.GetWechatArticles
import java.util.HashMap

/**
 * author jingting
 * date : 2020-06-1711:20
 */
class GetTopArticlesRequest : Request() {

    private var article_id: Int = 0

    private var page: Int = 0

    override fun method(): Int {
        return GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(GetTopArticles::class.java)
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        return if (buildAuthParams(params)) {
            params
        } else super.params()
    }

    override fun url(): String {
        return "$URL/json"
    }

    companion object {
        private val URL = GifFun.BASE_WAN_URL + "article/top"
    }
}