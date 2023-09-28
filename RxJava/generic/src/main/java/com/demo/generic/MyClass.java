package com.demo.generic;

public class MyClass {

  /*
    泛型
   */
    public static void main(String[] args) {
      NormalGeneric<String> generic = new NormalGeneric<>();

      generic.setData("haha");
      System.out.println(generic.getData());
    }
}