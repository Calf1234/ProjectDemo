package com.demo.jetpack.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.jetpack.R
import com.demo.jetpack.ui.base.BaseFragment


/**
 * 抽屉的 左侧半界面
 */
class DrawerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }
}