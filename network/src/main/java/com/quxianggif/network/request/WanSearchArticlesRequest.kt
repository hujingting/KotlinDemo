package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetWechatArticles
import com.quxianggif.network.model.WanSearchArticles
import com.quxianggif.network.util.NetworkConst
import java.util.HashMap

/**
 * author jingting
 * date : 2020-06-1711:20
 */
class WanSearchArticlesRequest : Request() {

    private var article_id: Int = 0

    private var page: Int = 0

    private var key_word = ""

    fun aritcleId(article_id : Int) : WanSearchArticlesRequest {
        this.article_id = article_id
        return  this
    }

    fun page(page: Int) : WanSearchArticlesRequest {
        this.page = page
        return  this
    }

    fun keyWord(key_word: String) : WanSearchArticlesRequest {
        this.key_word = key_word
        return this
    }

    override fun method(): Int {
        return GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(WanSearchArticles::class.java)
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        return if (buildAuthParams(params)) {
            params[NetworkConst.SEARCH_KEY] = key_word
            return params
        } else super.params()
    }

    override fun url(): String {
        return "$URL$article_id/$page/json"
    }

    companion object {
        private val URL = GifFun.BASE_WAN_URL + "wxarticle/list/"
    }
}