package com.demo.rxjava.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static String BASE_URL = "https://www.wanandroid.com/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static Retrofit getOnlineCookieRetrofit() {
        // OKHttp客户端
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        // 各种参数配置
        OkHttpClient okHttpClient = httpBuilder
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                // 请求用okhttp
                .client(okHttpClient)
                // 相应rxjava
                // 添加一个json解析的工具
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                // 添加rxjava处理工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 封装操作
     * @param <UD> 上游
     * @return
     */
    public static final <UD> ObservableTransformer<UD, UD> rxud() {
        return new ObservableTransformer<UD, UD>() {
            @NonNull
            @Override
            public ObservableSource<UD> apply(@NonNull Observable<UD> upstream) {
                return upstream.subscribeOn(Schedulers.io())// 给上面代码分配异步线程
                        .observeOn(AndroidSchedulers.mainThread()) // 给下面代码分配主线程;
                        .map(new Function<UD, UD>() {
                            @NonNull
                            @Override
                            public UD apply(@NonNull UD ud) throws Exception {
                                Log.d(TAG, "apply: 监听到 配置了线程切换");
                                return ud;
                            }
                        });
            }
        };
    }
}
