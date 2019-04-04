package com.tbs.giffun.activity

import android.app.Activity
import android.content.Intent
import android.view.View

class UserHomePageActivity: BaseActivity() {


    companion object {

        private const val TAG = "UserHomePageActivity"

        const val USER_ID = "user_id"

        const val NICKNAME = "NICKNAME"

        const val AVATAR = "avatar"

        const val BG_IMAGE = "bg_image"

        fun actionStart(activity: Activity, image: View?, userId: Long, nickname: String, avatar: String, bgImage: String) {
            val intent = Intent(activity, UserHomePageActivity::class.java)
            intent.putExtra(USER_ID, userId)
            intent.putExtra(NICKNAME, nickname)
            intent.putExtra(AVATAR, avatar)
            intent.putExtra(BG_IMAGE, bgImage)
            activity.startActivity(intent)

            //        if (AndroidVersion.hasLollipop()) {
            //            ActivityOptions options =
            //                    ActivityOptions.makeSceneTransitionAnimation(activity,
            //                            Pair.create(image, GlobalUtil.getString(R.string.transition_user_home_page_avatar)));
            //
            //            activity.startActivity(intent, options.toBundle());
            //        } else {
            //            activity.startActivity(intent);
            //        }
        }
    }
}