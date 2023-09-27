package com.demo.mrxjava;

// todo 观察者 下游
public interface Observer<T>  {
    public void onSubscribe();

    public void onNext(T item);  // Int

    public void onError(Throwable e);

    public void onComplete();
}
