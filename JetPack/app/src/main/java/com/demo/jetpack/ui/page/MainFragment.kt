package com.demo.jetpack.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.jetpack.R
import com.demo.jetpack.bridge.data.bean.TestAlbum
import com.demo.jetpack.bridge.request.MusicRequestViewModel
import com.demo.jetpack.bridge.state.MainViewModel
import com.demo.jetpack.databinding.AdapterPlayItemBinding
import com.demo.jetpack.databinding.FragmentMainBinding
import com.demo.jetpack.ui.base.BaseFragment
import com.xiangxue.architecture.ui.adapter.SimpleBaseBindingAdapter

// 首页画面的Fragment 真正显示
class MainFragment : BaseFragment() {

    // 我们操作布局，不去传统方式操作，全部使用Databindxxx
    private var mainBinding: FragmentMainBinding? = null
    private var mainViewModel: MainViewModel? = null // 首页Fragment的ViewModel

    private var musicRequestViewModel: MusicRequestViewModel? =
        null // 音乐资源相关的VM  todo Request ViewModel

    // 适配器
    private var adapter: SimpleBaseBindingAdapter<TestAlbum.TestMusic?, AdapterPlayItemBinding?>? =
        null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化ViewModel
        mainViewModel = getFragmentViewModelProvider(this).get(MainViewModel::class.java)
        musicRequestViewModel =
            getFragmentViewModelProvider(this).get(MusicRequestViewModel::class.java)
    }

    // 绑定DataBinding 与 ViewModel 关联
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        mainBinding = FragmentMainBinding.bind(view)
        mainBinding?.click = ClickProxy() // 设置点击事件，布局就可以直接绑定
        mainBinding?.setVm(mainViewModel) // 设置VM，就可以实时数据变化
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 触发  --->  MainFragment初始化页面的标记，初始化选项卡和页面
        mainViewModel!!.initTabAndPage.set(true)

        // 触发，---> 还要加载WebView
        mainViewModel!!.pageAssetPath.set("JetPack之 WorkManager.html")

        // 展示数据，适配器里面的的数据 展示出来
        // 设置设配器(item的布局 和 适配器的绑定)
        adapter = object : SimpleBaseBindingAdapter<TestAlbum.TestMusic?, AdapterPlayItemBinding?>(
            context,
            R.layout.adapter_play_item
        ) {
            override fun onSimpleBindItem(
                binding: AdapterPlayItemBinding?,
                item: TestAlbum.TestMusic?,
                holder: RecyclerView.ViewHolder?
            ) {

                binding?.tvTitle?.text = item?.title // 标题
                binding?.tvArtist?.text = item?.artist?.name // 歌手 就是 艺术家
                Glide.with(binding?.ivCover!!.context).load(item?.coverImg)
                    .into(binding.ivCover) // 左右边的图片

                // 歌曲下标记录
                // val currentIndex = PlayerManager.instance.albumIndex // 歌曲下标记录

                // 播放的标记
                /*binding.ivPlayStatus.setColor(
                    if (currentIndex == holder ?.adapterPosition) resources.getColor(R.color.colorAccent) else Color.TRANSPARENT
                ) // 播放的时候，右变状态图标就是红色， 如果对不上的时候，就是没有*/

                // 点击Item
                binding.root.setOnClickListener { v ->
                    Toast.makeText(mContext, "播放音乐", Toast.LENGTH_SHORT).show()
                    //  PlayerManager.instance.playAudio(holder !!.adapterPosition)
                }
            }
        }

        mainBinding!!.rv.adapter = adapter

        // 请求数据
        // 保证我们列表没有数据（music list）
        // if (PlayerManager.instance.album == null) {
        musicRequestViewModel!!.requestFreeMusics()
        // }

        // 眼睛 监听的变化，你只要敢变，UI就要变
        // 观察到了 observe
        // 音乐资源的 VM
        // 此处理解就是观察者， 有一双眼睛盯着看，getFreeMusicsLiveData变化了，如果变化就执行
        musicRequestViewModel!!.freeMusicesLiveData!!.observe(
            viewLifecycleOwner,
            { musicAlbum: TestAlbum? ->
                if (musicAlbum != null && musicAlbum.musics != null) {
                    // 这里特殊：直接更新UI，越快越好
                    adapter?.list = musicAlbum.musics // 数据加入适配器
                    adapter?.notifyDataSetChanged()

                    // 播放相关的业务需要这个数据
                    /*if (PlayerManager.instance.album == null ||
                        PlayerManager.instance.album !!.albumId != musicAlbum.albumId) {
                        PlayerManager.instance.loadAlbum(musicAlbum)
                    }*/
                }
            })
    }


    inner class ClickProxy {
        // 当在首页点击 “菜单” 的时候，直接导航到 ---> 菜单的Fragment界面
        fun openMenu() {
            sharedViewModel.openOrCloseDrawer.value = true
        } // 间接通过共享VM 触发到 openDrawer 触发到 @BindingAdapter

        // 当在首页点击 “搜索图标” 的时候，直接导航到 ---> 搜索的Fragment界面
        fun search() = nav().navigate(R.id.action_mainFragment_to_searchFragment)

    }

}