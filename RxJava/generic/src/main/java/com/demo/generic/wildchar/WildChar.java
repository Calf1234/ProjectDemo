package com.demo.generic.wildchar;

import java.util.HashMap;
import java.util.Map;

public class WildChar {
    public static void print(GenericType<Fruit> fruit) {
        System.out.println(fruit.getData());
    }


    public static void print2(GenericType<? extends Fruit> fruit) {
        System.out.println(fruit.getData());
    }

    /**
     * ? super Apple 数据的下界 指定是Apple，所有Apple的父类都满足，子类都不满足
     *
     * @param fruit
     */
    public static void print3(GenericType<? super Apple> fruit) {
        System.out.println(fruit.getData());
    }

    public static void main(String[] args) {
        /*
        Apple和Fruit有继承关系，但是泛型类没有关联
         */
//        print(new GenericType<Apple>());

        /*  ? 通配符
        ? extends Fruit
        extends Fruit：类型 要派生自Fruit，或者说指定了上界是Fruit

        extends 安全的访问数据
         */
        print2(new GenericType<Apple>());


        GenericType<Orange> orange = new GenericType<>();
        GenericType<? extends Fruit> fruit = orange;

        GenericType<Fruit> fruitGenericType = new GenericType<>();
        GenericType<Orange> orangeGenericType = new GenericType<>();
//        fruit.setData(fruit);
        Fruit data = fruit.getData();

//        print3(orangeGenericType);
        print3(fruitGenericType);
        print3(new GenericType<Apple>());
//        print3(new GenericType<Hongfushi>());

        GenericType<? super Apple> apple = new GenericType<>();
        apple.setData(new Apple());
        apple.setData(new Hongfushi());
//        apple.setData(new Fruit());
        Object appleData = apple.getData();


        Map<String, String> map = new HashMap<>();
    }
}
