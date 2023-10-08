package com.demo.jetpack.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.demo.jetpack.R
import com.demo.jetpack.bridge.state.MainActivityViewModel
import com.demo.jetpack.databinding.ActivityMainBinding
import com.demo.jetpack.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    var mainActivityViewModel:MainActivityViewModel ?= null
    var mainBinding:ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding?.lifecycleOwner = this;
        mainBinding?.vm = mainActivityViewModel
    }
}