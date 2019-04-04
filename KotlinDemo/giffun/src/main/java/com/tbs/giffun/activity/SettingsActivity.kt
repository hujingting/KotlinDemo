package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent

class SettingsActivity: BaseActivity() {

    companion object {

        private const val TAG = "SettingsActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }
}