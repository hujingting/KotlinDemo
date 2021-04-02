package com.quxianggif.hencoder

/**
 * author jingting
 * date : 2021/4/1下午5:20
 */

/**
 *
 * Kotlin 中实现单例类：
 *
 * 和 Java 相比的不同点有：
  1.和类的定义类似，但是把 class 换成了 object 。
  2.不需要额外维护一个实例变量 sInstance。
  3.不需要「保证实例只创建一次」的 getInstance() 方法。
  这种通过 object 实现的单例是一个饿汉式的单例，并且实现了线程安全。
 */
object TestInstance {

    var number: Int = 6

    fun method(){
        println("test")
    }
}