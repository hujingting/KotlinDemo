package com.quxianggif.hencoder

import android.content.res.Resources
import android.util.TypedValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * author jingting
 * date : 2020-08-1915:39
 */
class PracticeKotlin {

//    /**
//     * 协程 （一个线程框架）
//     */
//    private fun launch() {
//        val coroutineScope = CoroutineScope(this)
//        coroutineScope.launch(Dispatchers.IO) {
//            /**
//             * 如果只是使用 launch 函数，协程并不能比线程做更多的事。
//             * 不过协程中却有一个很实用的函数：withContext 。这个函数可以切换到指定的线程，
//             * 并在闭包内的逻辑执行结束之后，自动把线程切回去继续执行
//             */
//            val image = withContext(Dispatchers.IO) {
//                // 👈 切换到 IO 线程, 并在执行完成后切回 UI 线程
////            getImage()
//            }
//
//            // 👈 回到 UI 线程更新 UI
//            TODO()
//
//            /**
//             * 由于可以"自动切回来"，消除了并发代码在协作时的嵌套。
//             * 由于消除了嵌套关系，我们甚至可以把 withContext 放进一个单独的函数里面,
//             * 这就是之前说的「用同步的方式写异步的代码」了
//             */
////           val imageText = getImage(0)
//        }
//    }
//
//    /**
//     * suspend 是 Kotlin 协程最核心的关键字 表示「暂停」或者「可挂起」并且这个『挂起』是非阻塞式的，它不会阻塞你当前的线程。」
//     */
//    suspend fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
//
//    }
//
//
//    /**
//     * kotlin 的扩展函数
//     */
//    val Float.dp
//        get() = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                this,
//                Resources.getSystem().displayMetrics
//        )
//
//    val RADIUS = 200f.dp
//
//    private fun getImage(): Any {
//        return Any()
//    }
}