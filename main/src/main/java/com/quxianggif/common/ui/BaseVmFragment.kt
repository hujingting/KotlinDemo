package com.quxianggif.common.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * author jingting
 * date : 2021/5/13上午11:33
 *
 * mvvm 基础 fragment
 */
open class BaseVmFragment : Fragment(){

    /**
     * 开放给外部使用
     */
    lateinit var mContext: Context
    lateinit var mActivity: AppCompatActivity
    private var fragmentProvider: ViewModelProvider? = null
    private var activityProvider: ViewModelProvider? = null
//    private var dataBindingConfig: DataBindingConfig? = null
    private var mBinding: ViewDataBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as AppCompatActivity
        // 必须要在Activity与Fragment绑定后，因为如果Fragment可能获取的是Activity中ViewModel
        // 必须在onCreateView之前初始化viewModel，因为onCreateView中需要通过ViewModel与DataBinding绑定
        initViewModel()
//        ParamUtil.initParam(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observe一定要在初始化最后，因为observe会收到黏性事件，随后对ui做处理
        observe()
    }

    /**
     * 初始化viewModel
     * 之所以没有设计为抽象，是因为部分简单activity可能不需要viewModel
     * observe同理
     */
    open fun initViewModel() {

    }

    /**
     * 注册观察者
     */
    open fun observe() {

    }

}