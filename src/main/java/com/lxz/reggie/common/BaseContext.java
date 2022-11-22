package com.lxz.reggie.common;

// 创建这个类的原因是想要通过threadlocal获取用户的id，因为我们使用了MetaObjectHandler进行公共字段填充，
// 但是该方法没有request对象，因此无法通过session获取当前登录用户的ID
// 然而threadlocal方法可以通过filter中，获取登录用户的id，所以为了能在MetaObjectHandler中获取id，写了这个类

// 基于threadlocal创建的类，用于保存当前登录用户的ID
public class BaseContext {
    // 用户的id是long型，所以这里泛型为long型
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        // 通过调用set方法将数据存入threadlocal
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        // 通过get方法获取threadlocal内的数据
        return threadLocal.get();
    }
}
