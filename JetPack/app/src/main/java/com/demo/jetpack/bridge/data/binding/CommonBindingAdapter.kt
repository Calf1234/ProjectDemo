package com.demo.jetpack.bridge.data.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.xiangxue.architecture.utils.ClickUtils

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onClickWithDebouncing"], requireAll = false)
    fun onClickWithDebouncing(view: View?, clickListener: View.OnClickListener?) {
        ClickUtils.applySingleDebouncing(view, clickListener)
    }
}