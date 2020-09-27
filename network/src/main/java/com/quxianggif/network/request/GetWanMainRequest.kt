/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quxianggif.network.request

import com.quxianggif.core.GifFun
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.GetFollowings
import com.quxianggif.network.model.GetRecommendFollowing
import com.quxianggif.network.model.GetWanMain
import com.quxianggif.network.util.NetworkConst

import java.util.HashMap

import okhttp3.Headers

/**
 * 获取公众号列表
 *
 * @author guolin
 * @since 18/3/19
 */
class GetWanMainRequest : Request() {

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(GetWanMain::class.java)
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        return if (buildAuthParams(params)) {
            params
        } else super.params()
    }

    override fun headers(builder: Headers.Builder): Headers.Builder {
        buildAuthHeaders(builder, NetworkConst.DEVICE_SERIAL, NetworkConst.TOKEN)
        return super.headers(builder)
    }

    companion object {
        private val URL = GifFun.BASE_WAN_URL + "wxarticle/chapters/json"
    }

}
