package com.quxianggif.common.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quxianggif.ui.base.DataBindingConfig

/**
 * author jingting
 * date : 2021/5/13上午11:33
 *
 * mvvm 基础 fragment
 */
abstract class BaseVmFragment : Fragment(){

    /**
     * 开放给外部使用
     */
    lateinit var mContext: Context
    lateinit var mActivity: AppCompatActivity
    private var fragmentProvider: ViewModelProvider? = null
    private var activityProvider: ViewModelProvider? = null
    private var dataBindingConfig: DataBindingConfig? = null
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        getLayoutId()?.let {
//            setStatusColor()
//            setSystemInvadeBlack()
            //获取ViewDataBinding
            val binding: ViewDataBinding =
                    DataBindingUtil.inflate(inflater, it, container, false)
            //将ViewDataBinding生命周期与Fragment绑定
            binding.lifecycleOwner = viewLifecycleOwner
            dataBindingConfig = getDataBindingConfig()
            dataBindingConfig?.apply {
                val bindingParams = bindingParams
                // 将bindingParams逐个加入到ViewDataBinding中的Variable
                // 这一步很重要,否则xml中拿不到variable中内容
                for (i in 0 until bindingParams.size()) {
                    binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
                }
            }
            mBinding = binding
            return binding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun getLayoutId(): Int?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observe一定要在初始化最后，因为observe会收到黏性事件，随后对ui做处理
        observe()
    }


    /**
     * 通过activity获取viewModel，跟随activity生命周期
     */
    protected fun <T : ViewModel?> getActivityViewModel(modelClass: Class<T>): T {
        if (activityProvider == null) {
            activityProvider = ViewModelProvider(mActivity)
        }
        return activityProvider!!.get(modelClass)
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

    /**
     * 获取dataBinding配置项
     */
    abstract fun getDataBindingConfig(): DataBindingConfig?

}