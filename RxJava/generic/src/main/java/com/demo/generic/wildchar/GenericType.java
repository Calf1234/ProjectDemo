package com.demo.generic.wildchar;

public class GenericType<T> {
    T data;

    public GenericType() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
