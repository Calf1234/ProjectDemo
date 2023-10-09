package com.demo.jetpack.ui

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.demo.jetpack.R
import com.demo.jetpack.bridge.data.config.Configs
import com.demo.jetpack.bridge.state.MainActivityViewModel
import com.demo.jetpack.databinding.ActivityMainBinding
import com.demo.jetpack.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    var mainActivityViewModel: MainActivityViewModel? = null
    var mainBinding: ActivityMainBinding? = null

    private var isListened = false // 被倾听 为了扩展，目前还用不上


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = getActivityViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding?.lifecycleOwner = this;
        mainBinding?.vm = mainActivityViewModel

        // 共享 （观察） 活动关闭的一些记录（播放条 缩小一条 与 扩大展开）
        mSharedViewModel.activityCanBeClosedDirectly.observe(this, {
            // 先不写，作用不大，以后扩展用的
            Log.d(Configs.TAG, if (it) "中控中心 我知道了，原来播放条被收缩了" else "中控中心 我知道了，原来播放条被展开了")
        })

        // 间接的可以 打开菜单 关闭菜单（观察）
        mSharedViewModel.openOrCloseDrawer.observe(this, { aBoolean ->
            mainActivityViewModel!!.openDrawer.value = aBoolean // 触发，就会改变，---> 观察（打开菜单逻辑）
        })

        // 间接的xxxx （观察）  开启和关闭 卡片相关的状态，如果发生改变 会和 allowDrawerOpen 挂钩 (后续的扩展 用的字段)
        mSharedViewModel.enableSwipeDrawer.observe(this, { aBoolean ->
            mainActivityViewModel!!.allowDrawerOpen.value = aBoolean // 触发抽屉控件关联的值
        })
    }


    /**
     * 详情看：https://www.cnblogs.com/lijunamneg/archive/2013/01/19/2867532.html
     * 这个onWindowFocusChanged指的是这个Activity得到或者失去焦点的时候 就会call。。
     * 也就是说 如果你想要做一个Activity一加载完毕，就触发什么的话 完全可以用这个！！！
     *  entry: onStart---->onResume---->onAttachedToWindow----------->onWindowVisibilityChanged--visibility=0---------->onWindowFocusChanged(true)------->
     * @param hasFocus
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!isListened) {
            // 此字段只要发生了改变，就会 添加监听（可以弹上来的监听）
            mSharedViewModel.timeToAddSlideListener.value = true // 触发改变
            isListened = true // 被倾听 修改成true
        }
    }

    override fun onBackPressed() {
        if (mSharedViewModel.closeSlidePanelIfExpanded.value == true) {
            super.onBackPressed()
        } else {
            // 触发改变 true 如果此时是 播放详情，会被掉下来
            mSharedViewModel.closeSlidePanelIfExpanded.value = true
        }

    }
}