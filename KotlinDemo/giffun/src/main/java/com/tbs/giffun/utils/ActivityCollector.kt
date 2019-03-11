package com.tbs.giffun.utils

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityCollector {

    private const val TAG = "ActivityCollector"

    private val activityList = ArrayList<WeakReference<Activity>?>()

    fun size(): Int {
        return activityList.size
    }

    fun add(weakReference: WeakReference<Activity>?) {
        activityList.add(weakReference)
    }

    fun remove(weakReference: WeakReference<Activity>?) {
        activityList.remove(weakReference)
    }

    fun finishAll() {
        if (activityList.isNotEmpty()) {
            for (weakReference in activityList) {
                val activity = weakReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }

            activityList.clear()
        }
    }
}