package com.demo.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.rxjava.api.WangAndroidApi;
import com.demo.rxjava.bean.ProjectBean;
import com.demo.rxjava.bean.ProjectItem;
import com.demo.rxjava.utils.HttpUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private WangAndroidApi api;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = HttpUtils.getOnlineCookieRetrofit().create(WangAndroidApi.class);

//        getProject(api);

        btn = findViewById(R.id.button);

        antiShakeActonUpdate();
    }

    /**
     * 功能防抖，网络请求，网络嵌套
     */
    private void antiShakeActon() {
        RxView.clicks(btn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)// 2秒钟之内 响应你一次
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@androidx.annotation.NonNull Object o) throws Exception {
                        api.getProject()
                                .subscribeOn(Schedulers.io())// 给上面代码分配异步线程
                                .observeOn(AndroidSchedulers.mainThread()) // 给下面代码分配主线程;
                                .subscribe(new Consumer<ProjectBean>() {
                                    @Override
                                    public void accept(@androidx.annotation.NonNull ProjectBean projectBean) throws Exception {
                                        for (ProjectBean.DataBean dataBean :
                                                projectBean.getData()) {
                                            api.getProjectItem(1, dataBean.getId())
                                                    .compose(HttpUtils.rxud())
                                                    .subscribe(new Consumer<ProjectItem>() {
                                                        @Override
                                                        public void accept(@androidx.annotation.NonNull ProjectItem projectItem) throws Exception {
                                                            // 网络嵌套
                                                            Log.d(TAG, "accept: " + projectBean);
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    /**
     * 功能防抖，网络请求，处理网络嵌套
     */
    private void antiShakeActonUpdate() {
        final int[] cnt = {0};

        RxView.clicks(btn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)// 2秒钟之内 响应你一次
                // 我只给下面 切换 异步
                .observeOn(Schedulers.io())
                .flatMap(new Function<Object, ObservableSource<ProjectBean>>() {

                    @androidx.annotation.NonNull
                    @Override
                    public ObservableSource<ProjectBean> apply(@androidx.annotation.NonNull Object o) throws Exception {
                        return api.getProject();
                    }
                })
                .flatMap(new Function<ProjectBean, ObservableSource<ProjectBean.DataBean>>() {
                    @androidx.annotation.NonNull
                    @Override
                    public ObservableSource<ProjectBean.DataBean> apply(@androidx.annotation.NonNull ProjectBean projectBean) throws Exception {
                        Log.d(TAG, "apply: size: " + projectBean.getData().size());
                        cnt[0] = 0;
                        return Observable.fromIterable(projectBean.getData()); // 我自己搞一个发射器 发多次 10
                    }
                })
                .flatMap(new Function<ProjectBean.DataBean, ObservableSource<ProjectItem>>() {
                    @androidx.annotation.NonNull
                    @Override
                    public ObservableSource<ProjectItem> apply(@androidx.annotation.NonNull ProjectBean.DataBean dataBean) throws Exception {
                        cnt[0]++;
                        Log.d(TAG, "apply: item: " + cnt[0] + " {page: 1, id: " + dataBean.getId() + "}");
                        return api.getProjectItem(1, dataBean.getId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 给下面切换 主线程
                .subscribe(new Consumer<ProjectItem>() {
                    @Override
                    public void accept(@androidx.annotation.NonNull ProjectItem projectItem) throws Exception {
                        Log.d(TAG, "accept: " + projectItem);
                    }
                });
    }

    private void getProject(WangAndroidApi api) {
        api.getProject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // 比较繁琐 接收方式1
//            .subscribe(new Observer<ProjectBean>() {
//                @Override
//                public void onSubscribe(@NonNull Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(@NonNull ProjectBean projectBean) {
//
//                }
//
//                @Override
//                public void onError(@NonNull Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            })
                // 简洁方式 接收方式2
                .subscribe(new Consumer<ProjectBean>() {
                    @Override
                    public void accept(@NonNull ProjectBean projectBean) throws Exception {

                    }
                })
        // 最简洁方式，可读性差 接收方式3
//        .subscribe(v->{
//
//        })
        ;
        return;
    }

    private void getProjectItem(int page, int cid) {
        api.getProjectItem(page, cid)
                /* 简洁方式，设置了请求时在子线程，处理相应在主线程
                等价于
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                */
                .compose(HttpUtils.rxud())
                .subscribe(new Consumer<ProjectItem>() {
                    @Override
                    public void accept(@androidx.annotation.NonNull ProjectItem projectItem) throws Exception {

                    }
                });
    }
}