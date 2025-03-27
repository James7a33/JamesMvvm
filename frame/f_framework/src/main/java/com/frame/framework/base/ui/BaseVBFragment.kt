package com.frame.framework.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.frame.framework.base.view.BaseIView
import com.frame.framework.base.vm.BaseViewModel
import com.frame.framework.ext.inflateBinding

/**
 * @Author: james
 * @Date: 2023/7/25 18:58
 * @Description: ViewModel+ViewBinding BaseFragment
 */
abstract class BaseVBFragment<VM : BaseViewModel, VB : ViewBinding> : BaseVMFragment<VM>(),
    BaseIView {

    private var _binding: VB? = null
    val bind: VB get() = _binding!!

    /**
     * 创建 ViewBinding
     */
    override fun initViewDataBind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = inflateBinding(inflater, container, false)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}