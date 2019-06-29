package com.tbs.kotlinpractice

import java.util.*

object MyClass {

    @JvmStatic
    fun main(args: Array<String>) {

//        whileTest()
//
//        println(whenTest(1))
//        println(whenTest("hello"))
//        println(whenTest(100L))
//        println(whenTest(2))
//        println(whenTest("jt"))
//
//        /**
//         * 有一个英文小写单词列表 List\，要求将其按首字母分组（key 为 ‘a’ - ‘z’），并且每个分组内的单词列表都是按升序排序，得到一个 Map\>
//         */
//        var list = arrayListOf<String>("fruit", "banana", "apple", "pea", "orange", "abc", "opq")
//        sortListByFirstChar(list)


//        for (i in 1..3) {
//            print(i)
//        }


//        for (i in 6 downTo 0 step 2) {
//            println(i)
//        }

//        val array = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")
//
//        for (i in array.indices) {
//            print(array[i])
//        }

//        foo()

//        for (i in 1..100) {
//            println(fizzBuzz(i))
//        }


        //初始化并迭代map
//        iterationMap()

        //使用 in 检查区间的成员
//        println(isLetter('c'))
//        println(isNotDigit('c'))
//        println("Kotlin" in "J".."S")
        //in 检查也适应与集合
//        println("Kotlin" in setOf("Java", "Kotlin"))

        /**
         * kotlin中的异常 throw结构是一个表达式
         */
//        var number = 1
//        val percentage =
//                if (number in 0..100)
//                    number
//                else
//                    throw IllegalArgumentException(
//                            "A percentage value must be ..."
//                    )

//        println("12.3456.A.B".split("\\.|-".toRegex()))

//        parsePath("/Users/desktop/pic/dog.png")

//        val button = Button()
//        button.showOff()
//        button.click()

//        val bob = User("bruce")
//        val ben = User("ben", false)
//        val mark = User("mark", isSub = false)

//        alphabet()

//        printAllCaps()

//        val ceo = Employee("Bruce", null)
//        val dev = Employee("Tom", ceo)
//        println(managerName(ceo))
//        println(managerName(dev))

//        ignoreNulls("test")

//        val email:String? = "email"
////        if (email != null) {
////            sendEmailTo(email)
////        }
//        email?.let { sendEmailTo(it) }


//        val list = listOf(1, 2, 3, 4)
//        println(list.filter { it % 2 == 0 })
//
//        val people = listOf(Person("张三", 20), Person("李四", 30))
//        println(people.filter { it.age >= 30})
//
//        println(list.map { it * it })
//        println(people.map { it.name })
//
//        val maxAge = people.maxBy(Person::age)!!.age

//        val numbers = mapOf(0 to "zero", 1 to "one")
//        println(numbers.mapValues { it.value.toUpperCase() })

//        val canBeInClub27 = { p: Person -> p.age <= 27 }
//        val people = listOf(Person("张三", 20), Person("李四", 30))
//        println(people.all(canBeInClub27))
//        println(people.any(canBeInClub27))
//        println(people.count(canBeInClub27))
//        println(people.filter(canBeInClub27).size)
//        println(people.find(canBeInClub27))

//        val people = listOf(Person("张三", 20), Person("李四", 30), Person("王五", 20), Person("小莉", 30));
//        println(people.groupBy { it.age })

//        val list = listOf("a", "abc", "b")
//        println(list.groupBy(String::first))


//        val strings = listOf("abc", "def", "abc")
//        println(strings.flatMap { it.toList().toSet() })


        //为了提高效率，可以把操作变成序列
        val people = listOf(Person("张三", 20), Person("李四", 30), Person("王五", 20), Person("小莉", 30));
        println(people.asSequence()
                .map(Person::name)
                .filter { it.startsWith("张") }
                .toList())
    }

    class MyService {
        fun performAction(): String = "foo"
    }

    class MyTest {

        /**
         * 申明一个不需要初始化器的非空类型的属性
         */
        private lateinit var myService: MyService
    }

    fun ignoreNulls(s: String?) {
        val sNotNull: String = s!!
        println(sNotNull.length)
    }

    fun sendEmailTo(email: String) {
        println("Sending email to $email")
    }

//    fun printAllCaps(s: String?) {
//        val allCaps: String? = s?.toUpperCase()
//        println(allCaps)
//    }

    class Employee(val name: String, val manager: Employee?)

    fun managerName(employee: Employee): String? = employee.manager?.name

    fun alphabet(): String {
        val stringBuilder = StringBuilder()
        return with(stringBuilder) {
            for (letter in 'A'..'Z') {
                append(letter)
            }

            append("l konw the alphabet!")
            toString()
        }
    }


//    fun

//    fun parsePath(path: String) {
//        val directory = path.substringBeforeLast("/")
//        val fullName = path.substringAfterLast("/")
//        val fileName = fullName.substringBeforeLast(".")
//        val extension = fullName.substringAfterLast(".")
//
//        println("Dir: $directory, name: $fileName, extension: $extension")
//    }

    private fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'

    fun isNotDigit(c: Char) = c !in '0'..'9'

    fun iterationMap() {
        val binaryReps = TreeMap<Char, String>()
        for (c in 'A'..'F') {
            val binary = Integer.toBinaryString(c.toInt())
            binaryReps[c] = binary
        }

        for ((letter, binary) in binaryReps) {
            println("$letter = $binary")
        }
    }

//    fun foo() {
//
//        listOf(1, 2, 3, 4, 5).forEach {
//
//            if (it == 3) return // 非局部直接返回到 foo() 的调用者
//
//            print(it)
//
//        }
//
//        println("this point is unreachable")
//
//    }

    fun foo() {

        listOf(1, 2, 3, 4, 5).forEach lit@{

            if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者，即 forEach 循环

            print(it)

        }

        print(" done with explicit label")

    }

    private fun whenTest(obj: Any): String {
        when (obj) {
            1 -> "one"
            "hello" -> return "two"
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


    private fun sortListByFirstChar(list: ArrayList<String>) {
        val result = list
                .groupBy { it[0] }
                .mapValues { it.value.sorted() }

        print(result.toString())
    }


    fun fizzBuzz(i: Int) = when {
        i % 15 == 0 -> "FizzBuzz"
        i % 3 == 0 -> "Fizz"
        i % 5 == 0 -> "Buzz"
        else -> "$i"
    }


}
