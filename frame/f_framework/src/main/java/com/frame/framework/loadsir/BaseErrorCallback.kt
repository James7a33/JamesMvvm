package com.frame.framework.loadsir

import com.kingja.loadsir.callback.Callback
import com.frame.framework.R

/**
 * @author: james
 * @Description:
 * @Date: 2023/8/1 14:51
 */
class BaseErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_base_error
    }

}