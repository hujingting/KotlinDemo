package com.quxianggif.hencoder

/**
 * author jingting
 * date : 2021/4/1下午5:38
 */


/**
 * Java 静态变量和方法的等价写法：companion object 变量和函数。
 *
 * 用 object 修饰的对象中的变量和函数都是静态的，但有时候，我们只想让类中的一部分函数和变量是静态的该怎么做呢?
 *
 * 可以使用companion 理解为伴随、伴生，表示修饰的对象和外部类绑定。
 *
 * (但这里有一个小限制：一个类中最多只可以有一个伴生对象，但可以有多个嵌套对象。就像皇帝后宫佳丽三千，但皇后只有一个。)
 */

class TestCompanionObject {

    companion object Test {
        var c: Int = 1
    }



}