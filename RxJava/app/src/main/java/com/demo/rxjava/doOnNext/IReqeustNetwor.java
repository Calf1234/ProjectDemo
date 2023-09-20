package com.demo.rxjava.doOnNext;

import io.reactivex.Observable;
import retrofit2.http.Body;

public interface IReqeustNetwor {

    // 请求注册 功能  todo 耗时操作 ---> OkHttp
    public io.reactivex.Observable<RegisterResponse> registerAction(@Body RegisterRequest registerRequest);

    // 请求登录 功能 todo 耗时操作 ---> OKHttp
    public Observable<LoginResponse> loginAction(@Body LoginReqeust loginRequest);

}
