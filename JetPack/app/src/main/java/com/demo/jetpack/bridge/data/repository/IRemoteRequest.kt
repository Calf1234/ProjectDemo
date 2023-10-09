package com.demo.jetpack.bridge.data.repository

import androidx.lifecycle.MutableLiveData
import com.demo.jetpack.bridge.data.bean.DownloadFile
import com.demo.jetpack.bridge.data.bean.LibraryInfo
import com.demo.jetpack.bridge.data.bean.TestAlbum

/**
 * 远程请求标准接口（在仓库里面）
 * 只为 HttpRequestManager 服务
 */
interface IRemoteRequest {

    fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?)

    fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>?)

    // 下载文件
    fun downloadFile(liveData: MutableLiveData<DownloadFile>?)
}