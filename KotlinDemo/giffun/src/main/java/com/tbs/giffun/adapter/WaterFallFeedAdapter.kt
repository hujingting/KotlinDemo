package com.tbs.giffun.adapter

import android.annotation.TargetApi
import android.app.Activity
import android.opengl.Visibility
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.ViewUtils
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.quxianggif.core.model.WaterFallFeed
import com.quxianggif.core.util.AndroidVersion
import com.quxianggif.network.model.LikeFeed
import com.tbs.giffun.R
import com.tbs.giffun.activity.UserHomePageActivity
import com.tbs.giffun.event.LikeFeedEvent
import com.tbs.giffun.holder.LoadingMoreViewHolder
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

abstract class WaterFallFeedAdapter<T : WaterFallFeed>(protected var activity: Activity, private val feedList: List<T>, private val imageWidth: Int,
                                                       private val layoutManager: RecyclerView.LayoutManager?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataItemCount: Int
        get() = feedList.size

    abstract var isNoMoreData: Boolean

    abstract var isLoadFailed: Boolean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_FEEDS -> return createFeedHolder(parent)
            TYPE_LOADING_MORE -> return createLoadingMoreHolder(parent)
        }

        throw IllegalArgumentException()
    }

    private fun createLoadingMoreHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = LoadingMoreViewHolder.createLoadingMoreViewHolder(activity, parent)
        holder.failed.setOnClickListener {
            onLoad()
            notifyItemChanged(itemCount - 1)
        }

        return holder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FeedViewHolder) {
            bindFeedHolder(holder, position)
        } else if (holder is LoadingMoreViewHolder) {
            bindLoadingMoreHolder(holder)
        }
    }

    private fun bindLoadingMoreHolder(holder: LoadingMoreViewHolder) {
        when {
            isNoMoreData -> {
                holder.failed.visibility = View.GONE
                holder.progress.visibility = View.GONE
                holder.end.visibility = View.VISIBLE
            }

            isLoadFailed -> {
                holder.failed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
                holder.end.visibility = View.GONE
            }

            else -> {
                holder.progress.visibility = View.VISIBLE
                holder.failed.visibility = View.GONE
                holder.end.visibility = View.GONE
            }
        }
    }

    protected fun baseCreateFeedHolder(holder: FeedViewHolder) {
        holder.cardView.setOnClickListener { v->
            if (AndroidVersion.hasLollipopMR1()) {
                setFabTransition(holder.feedCover)
            }

            val position = holder.adapterPosition
            val feed = feedList[position]
            if (feed.coverLoadFailed) {
                loadFeedCover(feed, holder, calculateImageHeight(feed))
                return@setOnClickListener
            }

            if (!feed.coverLoaded) {
                return@setOnClickListener
            }

            val coverImage = v.findViewById<ImageView>(R.id.feedCover)
//            FeedDetailActivity.actionStart(activity, coverImage, feed)
        }

        holder.likesLayout.setOnClickListener {
            val position = holder.adapterPosition
            val feed = feedList[position]
            var likesCount = feed.likesCount
            val event = LikeFeedEvent()
            if (this@WaterFallFeedAdapter is WorldFeedAdapter) {
                event.from = LikeFeedEvent.FROM_WORLD
            }
//            else if (this@WaterFallFeedAdapter is HotFeedAdapter) {
//                event.from = LikeFeedEvent.FROM_HOT
//            }

            event.feedId = feed.feedId
            if (feed.isLikedAlready) {
                feed.isLikedAlready = false
                likesCount -= 1
                if (likesCount < 0) {
                    likesCount = 0
                }
                feed.likesCount = likesCount
                event.type = LikeFeedEvent.UNLIKE_FEED
            } else {
                feed.isLikedAlready = true
                feed.likesCount = ++likesCount
                event.type = LikeFeedEvent.LIKE_FEED
            }
            notifyItemChanged(position)
            LikeFeed.getResponse(feed.feedId, null)
            event.likesCount = likesCount
            EventBus.getDefault().post(event)
        }

        val userInfoListener = View.OnClickListener {
            val position = holder.adapterPosition
            val feed = feedList[position]
            UserHomePageActivity.actionStart(activity, holder.avatar, feed.userId, feed.nickname, feed.avatar, feed.bgImage)
        }
        holder.avatar.setOnClickListener(userInfoListener)
        holder.nickname.setOnClickListener(userInfoListener)
    }


    private fun calculateImageHeight(feed: WaterFallFeed): Int {
        val originalWidth = feed.imgWidth
        val originalHeight = feed.imgHeight
        return imageWidth * originalHeight / originalWidth
    }

    private fun loadFeedCover(feed: T, holder: FeedViewHolder, imageHeight: Int) {
        Glide.with(activity)
                .load(feed.cover)
                .override(imageWidth, imageHeight)
                .placeholder(R.drawable.loading_bg_rect)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        feed.coverLoaded = false
                        feed.coverLoadFailed = true
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        feed.coverLoaded = true
                        feed.coverLoadFailed = false
                        return false
                    }

                })
                .into(holder.feedCover)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setFabTransition(item: View) {
//        val fab = activity.findViewById<View>(R.id.composeFab)
//        if (!ViewUtils.viewsIntersect(item, fab)) return
//
//        val reenter = TransitionInflater.from(activity)
//                .inflateTransition(R.transition.compose_fab_reenter)
//        reenter.addListener(object : TransitionUtils.TransitionListenerAdapter() {
//            override fun onTransitionEnd(transition: Transition) {
//                activity.window.reenterTransition = null
//            }
//        })
//        activity.window.reenterTransition = reenter
    }

    abstract fun onLoad()

    abstract fun bindFeedHolder(holder: FeedViewHolder, position: Int)

    abstract fun createFeedHolder(parent: ViewGroup): FeedViewHolder

    open class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view as CardView
        val feedCover: ImageView = view.findViewById(R.id.feedCover)

        val feedContent: TextView = view.findViewById(R.id.feedContent)

        val avatar: ImageView = view.findViewById(R.id.avatar)

        val nickname: TextView = view.findViewById(R.id.nickname)

        val likes: ImageView = view.findViewById(R.id.likes)

        val likesCount: TextView = view.findViewById(R.id.likesCount)

        val likesLayout: LinearLayout = view.findViewById(R.id.likesLayout)
    }

    companion object {

        private const val TAG = "WaterFallFeedAdapter"

        const val TYPE_FEEDS = 0

        private const val TYPE_LOADING_MORE = 1
    }
}