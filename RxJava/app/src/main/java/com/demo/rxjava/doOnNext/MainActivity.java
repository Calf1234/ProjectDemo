package com.demo.rxjava.doOnNext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.rxjava.R;
import com.demo.rxjava.utils.HttpUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * doOnNext
 * 适用来回切换线程
 */
public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


    }

    public void request() {
        // 1.请求服务器注册操作
        // 2.注册完成之后，更新注册UI
        MyRetrofit.createRetrofit().create(IReqeustNetwor.class)
                .registerAction(new RegisterRequest())
                .compose(HttpUtils.rxud())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        // 更新注册相关的所有UI
                        // .....
                    }
                });

        // 分开写

        // 3.马上去登录服务器操作
        // 4.登录完成之后，更新登录的UI
        MyRetrofit.createRetrofit().create(IReqeustNetwor.class)
                .loginAction(new LoginReqeust())
                .compose(HttpUtils.rxud())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        // 更新登录相关的所有UI
                        // .....
                    }
                });
    }

    public void request2() {
        /**
         * 一行代码 实现需求
         * 需求：
         *   还有弹出加载
         *  * 1.请求服务器注册操作
         *  * 2.注册完成之后，更新注册UI
         *  * 3.马上去登录服务器操作
         *  * 4.登录完成之后，更新登录的UI
         */

        MyRetrofit.createRetrofit().create(IReqeustNetwor.class)
                .registerAction(new RegisterRequest()) // todo 2 注册请求
                .subscribeOn(Schedulers.io()) // 给上面分配线程
                .observeOn(AndroidSchedulers.mainThread()) // 给下面分配主线程
                .doOnNext(new Consumer<RegisterResponse>() { // todo 3 更新注册ui
                    @Override
                    public void accept(@NonNull RegisterResponse registerResponse) throws Exception {
                        // 注册完成之后，更新注册UI
                    }
                })
                .observeOn(Schedulers.io()) // 给下面分配线程
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() { // todo 4 登录请求
                    @NonNull
                    @Override
                    public ObservableSource<LoginResponse> apply(@NonNull RegisterResponse registerResponse) throws Exception {
                        Observable<LoginResponse> loginResponseObservable = MyRetrofit.createRetrofit().create(IReqeustNetwor.class)
                                .loginAction(new LoginReqeust());
                        return loginResponseObservable;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 给下面 执行主线程
                .subscribe(new Observer<LoginResponse>() { // todo 5 更新登录ui
                    @Override
                    public void onSubscribe(@NonNull Disposable d) { // todo 1 开始
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull LoginResponse loginResponse) {
                        // 登录完成之后，更新登录的UI
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() { // todo 6 结束

                    }
                })
        ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose(); // 防止泄露
            }
        }
    }
}