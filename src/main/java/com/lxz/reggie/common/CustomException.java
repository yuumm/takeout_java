package com.lxz.reggie.common;

// 该class用于自定义业务异常
public class CustomException extends RuntimeException {
    // message是我们想要传送进来的异常信息
    public CustomException(String message) {
        super(message);
    }
}
