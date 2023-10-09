package com.demo.jetpack.bridge.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.demo.jetpack.R
import com.demo.jetpack.bridge.data.bean.DownloadFile
import com.demo.jetpack.bridge.data.bean.LibraryInfo
import com.demo.jetpack.bridge.data.bean.TestAlbum
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiangxue.architecture.utils.Utils
import java.util.*

// 仓库角色
class HttpRequestManager private constructor() : ILoadRequest, IRemoteRequest {

    // 暂无使用到
    var responseCodeLiveData: MutableLiveData<String>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    // 仓库：liveData: MutableLiveData<TestAlbum>?  已经和  Request VM 的 LiveData是 同一份了
    override fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?) {
        val gson = Gson()
        val type = object : TypeToken<TestAlbum?>() {}.type
        val testAlbum =
            gson.fromJson<TestAlbum>(Utils.getApp().getString(R.string.free_music_json), type)

        // TODO 在这里可以请求网络
        // TODO 在这里可以请求网络
        // TODO 在这里可以请求数据库
        // .....

        // 子线程  协程  框架  liveData.postValue

        liveData!!.value = testAlbum
    }

    override fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>?) {
        val gson = Gson()
        val type = object : TypeToken<List<LibraryInfo?>?>() {}.type
        val list =
            gson.fromJson<List<LibraryInfo?>>(Utils.getApp().getString(R.string.library_json), type)
        liveData!!.value = list as List<LibraryInfo>?
    }

    /**
     * 搜索界面的时候讲
     * TODO：模拟下载任务:
     * 可分别用于 普通的请求，和可跟随页面生命周期叫停的请求，
     * 具体可见 ViewModel 和 UseCase 中的使用。
     *
     * @param liveData 从 Request-ViewModel 或 UseCase 注入 LiveData，用于 控制流程、回传进度、回传文件
     */
    override fun downloadFile(liveData: MutableLiveData<DownloadFile>?) {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层
                var downloadFile = liveData!!.value
                if (downloadFile == null) {
                    downloadFile = DownloadFile()
                }
                if (downloadFile.progress < 100) {
                    downloadFile.progress = downloadFile.progress + 1
                    Log.d("TAG", "下载进度 " + downloadFile.progress + "%")
                } else {
                    timer.cancel()
                    downloadFile.progress = 0
                    return
                }
                if (downloadFile.isForgive) {
                    timer.cancel()
                    downloadFile.progress = 0
                    return
                }
                liveData.postValue(downloadFile)
                downloadFile(liveData)
            }
        }
        timer.schedule(task, 100)
    }

    companion object {
        val instance = HttpRequestManager()
    }
}