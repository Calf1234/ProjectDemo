package com.demo.jetpack.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.jetpack.bridge.data.bean.TestAlbum
import com.demo.jetpack.bridge.data.repository.HttpRequestManager


/**
 * 音乐资源 请求 相关的 ViewModel（只负责 MainFragment）
 */
class MusicRequestViewModel : ViewModel(){
    // by lazy 我想手写出 这个效果
    // val age by lazy { 88 }

    var freeMusicesLiveData : MutableLiveData<TestAlbum>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    fun requestFreeMusics() {
        HttpRequestManager.instance.getFreeMusic(freeMusicesLiveData)
    }
}