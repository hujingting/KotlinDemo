package com.quxianggif.core.model

import com.google.gson.annotations.SerializedName

/**
 * author jingting
 * date : 2020-05-2914:24
 */
class Articles : Model() {

    /**
     * apkLink: "",
    audit: 1,
    author: "鸿洋",
    canEdit: false,
    chapterId: 408,
    chapterName: "鸿洋",
    collect: false,
    courseId: 13,
    desc: "",
    descMd: "",
    envelopePic: "",
    fresh: false,
    id: 13928,
    link: "https://mp.weixin.qq.com/s/ULSW2clH4AjpvlnwD3zgrQ",
    niceDate: "2天前",
    niceShareDate: "1天前",
    origin: "",
    prefix: "",
    projectLink: "",
    publishTime: 1592236800000,
    realSuperChapterId: 407,
    selfVisible: 0,
    shareDate: 1592310407000,
    shareUser: "",
    superChapterId: 408,
    superChapterName: "公众号",
    tags: [
    {
    name: "公众号",
    url: "/wxarticle/list/408/1"
    }
    ],
    title: "Android View 体系竟然还能这么理解？",
    type: 0,
    userId: -1,
    visible: 1,
    zan: 0
     */

    override val modelId: Long
        get() = id

    @SerializedName("id")
    var id : Long = 0

    @SerializedName("author")
    var author : String = ""

    @SerializedName("shareUser")
    var shareUser: String = ""

    @SerializedName("link")
    var link: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("niceDate")
    var niceDate: String = ""

    @SerializedName("superChapterName")
    var superChapterName: String = ""

    @SerializedName("niceShareDate")
    var niceShareDate: String = ""

}