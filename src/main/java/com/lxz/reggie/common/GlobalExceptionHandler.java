package com.lxz.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

// 全局的异常捕获
// ControllerAdvice会拦截到controller中的save等方法的报错
// (annotations = {RestController.class})表示会拦截上方有RestController注解的controller
@ControllerAdvice(annotations = {RestController.class, Controller.class})
// 由于要返回一个json数据的response，所以要用ResponseBody的注解
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    // 该注解表示这个方法要处理哪种类型的exception
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        // Duplicate entry意思是重复输入，当错误包含这两个单词的时候，表示写入的数据在数据库中已经存在
        // 下方if判断的是错误类型是否为重复输入
        if (ex.getMessage().contains("Duplicate entry")) {
            // 错误内容是：Duplicate entry 'zhangsan' for key 'employee.idx_username'
            // 我们要获取"zhangsan"这个数据用于返回给前端，那么就可以通过空格进行分隔，然后取出这个内容
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    // 捕获SetmealServiceImpl中的CustomException异常
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        // 通过ex.getMessage()可以获得throw new CustomException("当前菜系关联了套餐，不能删除");中的msg
        return R.error(ex.getMessage());
    }
}
