package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent
import com.tbs.giffun.model.Draft

class PostFeedActivity: BaseActivity() {

    companion object {
        private const val TAG = "PostFeedActivity"

        private const val INTENT_DRAFT = "intent_draft"

        private const val PICK_GIF = 1

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, PostFeedActivity::class.java)
            activity.startActivity(intent)
        }

        fun actionStart(activity: Activity, draft: Draft) {
            val intent = Intent(activity, PostFeedActivity::class.java)
            intent.putExtra(INTENT_DRAFT, draft)
            activity.startActivity(intent)
        }
    }

}