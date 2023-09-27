package com.demo.mrxjava;

import android.os.Handler;
import android.os.Looper;

// [给所有下游 切换Android主线程线程]
public class ObserverAndroidMain_On<T> implements ObservableOnSubscribe<T>{
    // 拿到上一层
    private ObservableOnSubscribe<T> source;

    public ObserverAndroidMain_On(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(Observer<? super T> observableEmitter) {// == Observer
        // 包裹是为了，往下执行的时候，优先执行我们的 包裹
        PackageObserver<T> packageObserver = new PackageObserver(observableEmitter);
        // 把包裹交给source
        source.subscribe(packageObserver);
    }

    private final class PackageObserver<T> implements Observer<T> {
        private Observer<T> observer; // 临时存储一份

        public PackageObserver(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T item) {
            // Handler
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 变成 Android main线程
                    observer.onNext(item);
                }
            });
        }

        @Override
        public void onError(Throwable e) {
            // Handler
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 变成 Android main线程
                    observer.onError(e);
                }
            });
        }

        @Override
        public void onComplete() {
            // Handler
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 变成 Android main线程
                    observer.onComplete();
                }
            });
        }
    }
}
