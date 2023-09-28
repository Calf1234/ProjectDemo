package com.demo.generic;

/**
 * 泛型类
 */
public class NormalGeneric<T> {
    private T data;

    public NormalGeneric() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
