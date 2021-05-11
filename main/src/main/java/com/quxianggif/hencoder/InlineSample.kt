package com.quxianggif.hencoder

/**
 * author jingting
 * date : 2021/5/11上午11:30
 *
 *  让变量内联用的是 const
 *
 *  kotlin 为了对函数进行内联的支持，给函数加上关键字 inline
 *  这个函数就会被以内联的方式进行编译
 *
 *  inline 是干嘛用的呢？ inline 关键字不止可以内联自己的内部代码，还可以内联自己内部的内部的代码。什么叫「内部的内部」？就是自己的函数类型的参数。
 *
 *  inline 是用来优化的吗？是，但你不能无脑使用它，你需要确定它可以带来优化再去用它，否则可能会变成负优化
 *  那怎么去做这个判断是否使用呢？很简单，如果你写的是高阶函数，会有函数类型的参数，加上 inline 就对了。
 *
 *  文章链接：https://rengwuxian.com/kotlin-source-noinline-crossinline/
 */
class InlineSample {

    inline fun hello (postAction: () -> Unit) {
        print("hello")
        postAction()
    }

    //调用处
    fun main() {
        hello {
            print("test")
        }
    }

    //实际编译的代码 (这个函数被编译器贴过来的时候是完全展开铺平的)
    fun mainReal() {
        print("hello")
        print("test")
    }
}