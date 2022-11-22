package com.lxz.reggie.Config;

// 配置MP的分页插件。在前端要使用分页查询的功能，可以使用MybatisPlus的分页查询插件来分页查询数据

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 由于是配置类，所以注解是configuration
@Configuration
public class MybatisPlusConfig {
    // 通过拦截器的方式将插件引入进来。拦截器和过滤器是不同的东西，但是基本的作用是一样的
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建拦截器对象
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 添加一个拦截器，使用mp内部的拦截器PaginationInnerInterceptor
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
