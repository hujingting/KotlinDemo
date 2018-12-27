package com.tbs.kotlinpractice

object MyClass {

    @JvmStatic
    fun main(args: Array<String>) {

        whileTest()

        println(whenTest(1))
        println(whenTest("hello"))
        println(whenTest(100L))
        println(whenTest(2))
        println(whenTest("jt"))
    }

    private fun whenTest(obj: Any): String{
        when (obj) {
            1 -> "one"
            "hello" ->  return "two"
            is Long -> return "long"
            !is String -> return "not a string"
            else -> return "unKnown"
        }
        return ""
    }

    private fun whileTest() {
        val items = listOf("apple", "banana", "pear")
        var index = 0
        while (index < items.size) {
            println("item at $index is ${items[index]}")
            index++
        }
    }
}
