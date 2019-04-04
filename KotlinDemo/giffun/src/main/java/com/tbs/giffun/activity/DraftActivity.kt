package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent

class DraftActivity: BaseActivity() {


    companion object {

        private const val TAG = "DraftActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, DraftActivity::class.java)
            activity.startActivity(intent)
        }
    }

}