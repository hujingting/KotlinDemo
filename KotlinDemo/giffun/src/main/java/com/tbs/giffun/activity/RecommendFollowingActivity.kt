package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent

class RecommendFollowingActivity: BaseActivity() {

    companion object {

        private const val TAG = "RecommendFollowingActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, RecommendFollowingActivity::class.java)
            activity.startActivity(intent)
        }

    }

}