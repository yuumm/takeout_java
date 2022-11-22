package com.lxz.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

// 这个类主要是实现返回的类。后端处理的所有数据都会封装成为这个对象
// 因为这个R类要尽可能的通用，所以就会设置泛型，满足各种需要
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
