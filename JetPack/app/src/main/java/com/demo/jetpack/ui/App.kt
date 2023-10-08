package com.demo.jetpack.ui

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.xiangxue.architecture.utils.Utils

/**
 * 整个项目的 Application
 */
class App:Application(), ViewModelStoreOwner {

    private var mAppViewModelStore:ViewModelStore ?=null
    private var mFactory: ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        // 此处 只会调用一次
        mAppViewModelStore = ViewModelStore()
    }

    // TODO 暴露出去 给外界用
    // 同学们注意，此函数只给 NavHostFragment 使用
    override fun getViewModelStore(): ViewModelStore= mAppViewModelStore!!


    // 监测下 Activity是否为null
    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

    // 监测下 Activity是否为null
    private fun checkActivity(fragment: Fragment): Activity? {
        return fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    // AndroidViewModelFactory 工程是为了创建ViewModel，给上面的getAppViewModelProvider函数用的
    private fun getAppFactory(activity: Activity):ViewModelProvider.Factory? {
        var application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory
    }

    // 关键函数，只暴露 给 BaseActivity 与 BaseFragment 用的，保证共享ViewModel初始化的 单例
    // 专门给 BaseActivity 与 BaseFragment 用的
    fun getAppViewModelProvider(activity: Activity):ViewModelProvider {
        return ViewModelProvider(
            (activity.application as App),
            (activity.application as App).getAppFactory(activity)!!
        )
    }
}