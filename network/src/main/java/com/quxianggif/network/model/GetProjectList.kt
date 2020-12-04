package com.quxianggif.network.model

import com.google.gson.annotations.SerializedName
import com.quxianggif.core.model.WeChatArticlesMain
import com.quxianggif.network.request.GetProjectRequest

/**
 * author jingting
 * date : 2020/12/4下午3:27
 */
class GetProjectList : Response() {

    @SerializedName("data")
    var projectList: WeChatArticlesMain = WeChatArticlesMain()

    companion object {
        fun getResponse(page: Int, id: Int, callback: Callback) {
            GetProjectRequest()
                    .page(page)
                    .id(id)
                    .listen(callback)
        }
    }


}