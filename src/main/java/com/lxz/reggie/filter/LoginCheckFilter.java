package com.lxz.reggie.filter;

// 这个包里面的类就是用作过滤器，主要是过滤用户没有登录的情况下就能访问资源

import com.alibaba.fastjson.JSON;
import com.lxz.reggie.common.BaseContext;
import com.lxz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// WebFilter是过滤器的注解。第一个参数表示过滤器的名称，第二个参数表示拦截哪些路径
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
// 下方注解是便于打log的注解
@Slf4j
public class LoginCheckFilter implements Filter {
    // 解决路径匹配不上的问题
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 后面要使用request.getRequestURI()，但是ServletRequest无法实现，
        // 因此要使用一次类型转换成为HttpServletRequest。后面response同理
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        1、获取本次请求的uri
        // Web上每种可用的资源，如 HTML文档、图像、视频片段、程序等都由一个通用资源标识符URI进行定位。
        // url是在浏览器地址输入栏的那一句字符串
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        // 下方的urls定义了哪些路径是不需要进行处理的
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                // 当路径是/backend/index.html的时候，会出现匹配不上的问题，这个时候前面的PATH_MATCHER就可以进行匹配
                "/backend/**",
                "/front/**",
                "/common/**"
        };

//        2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        log.info("本次请求{}无需处理", requestURI);

//        3、当不需要处理的时候直接放行
        if (check) {
            // filterChain的doFilter方法实现放行
            filterChain.doFilter(request, response);
            return;
        }

//        4、判断登录状态，若为登录状态，则直接放行
        // 通过判断session当中是否存有数据来判断用户是否登录
        if(request.getSession().getAttribute("employee") != null) {
            log.info("用户已经登录，id为{}", request.getSession().getAttribute("employee"));

            // 将数据存入threadlocal
            // ThreadLocal主要功能就是给每个线程创建变量副本，这样就可以保证一个线程对某个变量的修改不会影响到其他线程对该变量的使用。
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

//        5、如果没有登录，则通过输出流的方式响应数据（与前端商讨好要什么返回数据）
        // getWriter表示获取输出流，write表示写数据。
        // JSON.toJSONString(R.error("NOTLOGIN"))表示将R对象转换为json数据，然后通过输出流写出
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    // 检查请求路径是否可以放行的方法。其中requestURI是请求的路径
    public boolean check(String[] urls, String requestURI) {
        for (String url:urls) {
            // /backend/**使用的是通配符
            // 当路径是/backend/index.html的时候，会出现匹配不上的问题，这个时候前面的PATH_MATCHER就可以进行匹配
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
