package com.xzw.framework;

import com.xzw.framework.core.MyAnnotationConfigApplicationContext;

/**
 * @author maroon
 * @date 2023/3/23 20:35
 */
public class Test {
    public static void main(String[] args) {
        MyAnnotationConfigApplicationContext applicationContext = new MyAnnotationConfigApplicationContext("com.xzw.framework.entity");
        System.out.println(applicationContext.getBean("a"));
    }
}
