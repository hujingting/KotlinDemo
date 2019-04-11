package com.tbs.giffun.fragment

import com.quxianggif.core.model.WorldFeed

class WorldFeedsFragment: WorldFallFeedsFragment() {

  internal var feedList: MutableList<WorldFeed> = ArrayList()



  override fun onLoad() {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  /**
   * RecyclerView的数据源，用于存储所有展示中的Feeds
   */


  override fun setUpRecyclerView() {
   super.setUpRecyclerView()
//   adapter = 
  }

  override fun loadFeeds(lastFeed: Long) {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun refreshLoads() {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun refreshFeeds() {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun loadFeedsFromDB() {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun dataSetSize(): Int {
   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }


 }