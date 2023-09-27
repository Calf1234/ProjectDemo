package com.demo.mrxjava;


// todo 被观察者 上游
public class Observable<T> implements ObservableOnSubscribe<T> {

    ObservableOnSubscribe source;

    public Observable(ObservableOnSubscribe source) {
        this.source = source;
    }

    @Override
    public void subscribe(Observer<? super T> observableEmitter) {// == Observer
        // todo 2
        observableEmitter.onSubscribe();
        // todo 3
        source.subscribe(observableEmitter); // 这个source就有了  观察者 Observer
    }

    // 静态方法声明的<T>泛型        ObservableOnSubscribe<T>==静态方法声明的<T>泛型
    // 参数中：ObservableOnSubscribe<? extends T> 和可读可写模式没有任何关系，还是我们之前的那一套思想（上限和下限）
    public static <T> Observable<T> create(ObservableOnSubscribe<? extends T> source) {
        return new Observable<T>(source);
    }


    // 单一
    public static <T> Observable<T> just(final T t) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 发射用户传递的参数数据 去发射事件
                observableEmitter.onNext(t);

                // 调用完毕
                observableEmitter.onComplete(); // 发射完毕事件
            }
        });
    }

    // 2个参数
    public static <T> Observable<T> just(final T t, final T t2) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 发射用户传递的参数数据 去发射事件
                observableEmitter.onNext(t);
                observableEmitter.onNext(t2);

                // 调用完毕
                observableEmitter.onComplete(); // 发射完毕事件
            }
        });
    }

    // 1 2 3 4 可变参数
    public static <T> Observable<T> just(final T... t) { // just 内部发射了
        // 想办法让 source 是不为null的，  而我们的create操作符是，使用者自己传递进来的
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) { // observableEmitter == Observer
                for (T t1 : t) {

                    // Observer.onNext(1);

                    // 发射用户传递的参数数据 去发射事件
                    observableEmitter.onNext(t1);
                }

                // 调用完毕
                observableEmitter.onComplete(); // 发射完毕事件
            }
        });
    }

    /**
     * map变换操作符
     *
     * T == 上一层传递过来的类型  Integer  变换前的类型
     * R == 给一层的类型         String   变换后的类型
     *
     */
    public <R> Observable<R> map(Function<? super T, ? extends R> function) {// ? super T 可写模式
        ObservableMap<T, R> observableMap = new ObservableMap<>(source, function);// source 上一层的能力
        return new Observable<R>(observableMap); // source  该怎么来？     observableMap是source的实现类
    }

    // todo 给所有上游分配异步线程
    public Observable<T> observables_On() {
        // 实例化 处理上游的线程操作符
        return create(new ObservableOnIO(source));
    }

    // todo 给下游分配Android主线程
    public Observable<T> observers_AndroidMain_On() {
        // 实例化 处理下游的线程操作符
        ObserverAndroidMain_On<T> observerAndroidMain_on = new ObserverAndroidMain_On(source);
        return create(observerAndroidMain_on);
    }
}
