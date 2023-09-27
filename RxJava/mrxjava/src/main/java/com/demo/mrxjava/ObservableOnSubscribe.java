package com.demo.mrxjava;


public interface ObservableOnSubscribe<T> {

    // ? super  代表可写的   observableEmitter == 观察者
    public void subscribe(Observer<? super T> observableEmitter);
}
