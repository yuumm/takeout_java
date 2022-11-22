package com.lxz.reggie.Config;

// 该配置类主要是解决由于静态页面不在static和templet里面导致无法访问的问题

import com.lxz.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    // 实现静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        // addResourceHandler表示所有带有backend的url都允许访问，
        // addResourceLocations表示允许访问的文件是哪一个，classpath表示resources文件
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    // 当项目一启动就会执行config配置类的文件。当项目已启动，下面的方法就已经执行了，此时框架内部的转换器就已经被配置好了
    //  然后在通过add方法添加上我们自己的转换器
    // JacksonObjectMapper中解释了为什么要将数据从Long转换为String。进行转换除了要用到JacksonObjectMapper文件
    // 还需要配置下述方法作为消息转换器，将java对象转换为json数据
    // 下述方法扩展mvc的转换器
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建新的消息转换器。
        // 该转换器就是将controller中的返回值，如R.Success对象，转换成json格式，然后再通过输出流的方式返回给页面
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器。设置底层原理为JacksonObjectMapper
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将我们设置的转换器添加到框架的转换器列表中
        // 第一个参数0表示将我们的转换器设置为第一个，优先级最高的，这样会优先使用我们的转换器
        converters.add(0, messageConverter);
        super.extendMessageConverters(converters);
    }
}
