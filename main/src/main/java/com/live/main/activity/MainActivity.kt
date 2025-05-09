package com.live.main.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.frame.base.ui.BaseVBActivity
import com.frame.base.widget.MyViewPagerAdapter

import com.frame.common.constant.ARouterPath
import com.frame.common.constant.Constants
import com.frame.common.ext.getStringArrayExt
import com.frame.common.ext.getStringExt
import com.james.main.databinding.ActivityMainBinding
import com.live.main.R
import com.live.main.databinding.ActivityMainBinding
import com.live.main.vm.MainVM
import com.lxj.xpopup.enums.PopupAnimation
import com.tools.logger.logA
import com.tools.toast.ext.toastShort
import com.frame.res.R as Rs


@Route(path = ARouterPath.Main.MAIN)
class MainActivity : BaseVBActivity<MainVM, ActivityMainBinding>() {

    //退出时间
    private var lastPressTime: Long = 0

    //标题
    private var mTitles = getStringArrayExt(Rs.array.main_tab_array)

    //fragment
    private val mFragments = mutableListOf<Fragment>()

    //tab 实体类
    private var mTabEntities = ArrayList<CustomTabEntity>()

    //icon
    private var mIconNorIds = intArrayOf(
        R.mipmap.ic_tab_home_normal,
        R.mipmap.ic_tab_dynamic_normal,
        R.mipmap.ic_tab_message_normal,
        R.mipmap.ic_tab_mine_normal,
    )

    private var mIconPreIds = intArrayOf(
        R.mipmap.ic_tab_home_press,
        R.mipmap.ic_tab_dynamic_press,
        R.mipmap.ic_tab_message_press,
        R.mipmap.ic_tab_mine_press,
    )

    override fun titleBar(): String = ""

    override fun isTitleBarShow(): Boolean = false

    override fun initView(savedInstanceState: Bundle?) {
        initViewPager()
        onBackPress()
        initCallMeDialog()
    }

    private fun initViewPager() {
        mFragments.add(HomeFragment())
        mFragments.add(DynamicFragment())
        mFragments.add(MessageFragment())
        mFragments.add(MineFragment())
        mFragments.forEachIndexed { index, _ ->
            mTabEntities.add(TabEntity(mTitles[index], mIconPreIds[index], mIconNorIds[index]))
        }
        bind.commonTabLayout.apply {
            setTabData(mTabEntities)
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    bind.viewPager.currentItem = position
                }

                override fun onTabReselect(position: Int) {
                    "onTabReselect$position".logA("main")
                }
            })
        }

        bind.viewPager.apply {
            setCanScroll(false)
            adapter =
                MyViewPagerAdapter(supportFragmentManager, mFragments, mTitles.toMutableList())
            offscreenPageLimit = mFragments.size
            currentItem = 0
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val now = System.currentTimeMillis()
                if (lastPressTime == 0L || now - lastPressTime > Constants.App.APP_QUICK_TIME) {
                    getStringExt(Rs.string.again_logout).toastShort()
                    lastPressTime = now
                } else if (now - lastPressTime < Constants.App.APP_QUICK_TIME) {
                    moveTaskToBack(false)
                    // 触发系统默认的返回行为
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    /**
     * 1v1 呼叫弹框
     */
    private fun initCallMeDialog() {
        val dialog = customDialog(HomeCallMeDialog(this))
        dialog.hasShadowBg(false)
        dialog.popupAnimation(PopupAnimation.TranslateFromTop)
        dialog.offsetY(BarUtils.getStatusBarHeight())
        dialog.isCenterHorizontal(true)
    }

    override fun onRequestSuccess() {
        LiveEventBus.get("logout", Boolean::class.java).observe(this) {
            logout()
            IMChatUtils.getInstance().logout()
        }
    }
}