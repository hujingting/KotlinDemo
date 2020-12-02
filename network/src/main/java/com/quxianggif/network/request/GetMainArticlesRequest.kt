package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetMainArticles
import com.quxianggif.network.model.GetWechatArticles
import java.util.HashMap

/**
 * author jingting
 * date : 2020-06-1711:20
 */
class GetMainArticlesRequest : Request() {

    private var article_id: Int = 0

    private var page: Int = 0

    fun aritcleId(article_id : Int) : GetMainArticlesRequest {
        this.article_id = article_id
        return  this
    }

    fun page(page: Int) : GetMainArticlesRequest {
        this.page = page
        return  this
    }

    override fun method(): Int {
        return GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(GetMainArticles::class.java)
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        return if (buildAuthParams(params)) {
            params
        } else super.params()
    }

    override fun url(): String {
        return "$URL/$page/json"
    }

    companion object {
        private val URL = GifFun.BASE_WAN_URL + "article/list"
    }
}