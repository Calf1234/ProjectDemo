package com.demo.jetpack.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.demo.jetpack.bridge.callback.SharedViewModel
import com.demo.jetpack.ui.App

open class BaseFragment : Fragment() {
    protected var mActivity: AppCompatActivity? = null // 为了 让所有的子类 持有 Activity

    // private var _sharedViewModel: SharedViewModel
    // 贯穿整个项目的（只会让App(Application)初始化一次）
    protected lateinit var sharedViewModel: SharedViewModel // 共享区域的ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    // 给当前BaseFragment用的【共享区域的ViewModel】
    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (mActivity!!.applicationContext as App).getAppViewModelProvider(mActivity!!)
    }

    // 给所有的 子fragment提供的函数，可以顺利的拿到 ViewModel 【非共享区域的ViewModel】
    protected fun getFragmentViewModelProvider(fragment: Fragment): ViewModelProvider {
        return ViewModelProvider(fragment, fragment.defaultViewModelProviderFactory)
    }

    // 备用的
    // 给所有的 子fragment提供的函数，可以顺利的拿到 ViewModel 【非共享区域的ViewModel】
    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }

    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

}