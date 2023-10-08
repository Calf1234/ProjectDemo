package com.demo.jetpack.ui.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.jetpack.ui.App
import com.xiangxue.architecture.bridge.callback.SharedViewModel
import com.xiangxue.architecture.data.manager.NetworkStateManager
import com.xiangxue.architecture.utils.BarUtils

// open 剔除 final修饰
open class BaseActivity : AppCompatActivity() {
    // 贯穿整个项目的（只会让App(Application)初始化一次）
    protected lateinit var mSharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        // 需要用到工具
        // 给工具类初始化
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)

        // 准备：lifecycle
        // 意味着 BaseActivity被观察者  -----> NetworkStateManager观察者（一双眼睛 盯着看 onResume/onPause）
        // BaseActivity就是被观察者 ---> NetworkStateManager.getInstance()
        lifecycle.addObserver(NetworkStateManager.instance)
    }

    // 2020 用法 ViewModelProvider 【ViewModel共享区域】
    // 此getAppViewModelProvider函数，只给 共享的ViewModel用（例如：mSharedViewModel .... 等共享的ViewModel）
    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }

    // 此getActivityViewModelProvider函数，给所有的 BaseActivity 子类用的 【ViewModel非共享区域】
    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }
}