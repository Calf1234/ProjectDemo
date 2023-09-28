package com.demo.generic;

/**
 * 泛型方法
 */
public class GenericMethod {
    public <T> T genericMethod(T... a) {
        return a[a.length / 2];
    }

    /**
     * 限定类型变量
     * @param a
     * @param <T>
     * @return
     */
    public <T extends String> T genericMethod(T... a) {
        return a[a.length / 2];
    }

    public void test(int x, int y) {
        System.out.println(x + ", " + y);
    }

    public static void main(String[] args) {
        GenericMethod genericMethod = new GenericMethod();
        System.out.println(genericMethod.genericMethod("str1", "str2", "str3"));
//        System.out.println(genericMethod.<String>genericMethod("str1", "str2", "str3"));
    }
}
