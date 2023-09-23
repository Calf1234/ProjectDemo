package com.demo.rxjava.source;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.rxjava.R;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxjavaActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava1);

        /*
                    ObservableCreate(source0) extends Observabl {subscribeActual}   => observer4
                ObservableMap(source1,   => observer3
            ObservableSubscribeOn(source2,（即source1）  => observer2
                source2.subscribeActual执行在 subscribeOn分配的线程中
        ObservableObserveOn(source3,   => observer1
        .subscribe(Observer 自定义) => observer0

        1、执行 .subscribe时，source3.subscribe -> source2 -> source1 -> source0,
        2、.subscribe操作看子类实现的subscribeActual接口，目的是把接入入参的自定义Observer一层层包裹起来
           observer0 -> observer1 -> observer2 -> observer3 -> observer4
        3、observer4.onNext -> observer3.onNext ->  observer2.onNext -> observer1.onNext -> observer0.onNext
         */
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {

            }
        })
                // ObservableCreate.map
                .map(new Function<Object, Object>() {
                    @NonNull
                    @Override
                    public Object apply(@NonNull Object o) throws Exception {
                        return null;
                    }
                })
                // 给上面 执行线程
                /* scheduler.scheduleDirect(new SubscribeTask(parent))
                线程池 执行 Runnable
                 */
                .subscribeOn(Schedulers.io())
                // ObservableMap.subscribe
                // 给下面 执行主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        ;
    }

    public void test() {
        // Flowable 支持背压
//        Flowable.create()
        // 背压, 被观察者不停发，观察者来不及处理
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                while (true) {
                    e.onNext("haha");
                }
            }
        })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}