package com.shihHsin.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化過濾器
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 實作過濾器的功能
        log.info("LoginCheckFilter is working");
        filterChain.doFilter(servletRequest, servletResponse);   // 放行


        // 過濾器的後處理


//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//        HttpServletResponse servletResponse = (HttpServletResponse) response;
//
//        log.info(servletRequest.getRequestURI());
//        // 设置不要拦截的请求
//        String[] urls = new String[]{
//                "/employee/login",
//                "/employee/layout",
//                "/backend/**",
//                "/front/**",
//                "/swagger-ui.html",
//                "/user/code",
//                "/user/login"
//        };
    }
    @Override
    public void destroy() {
        // 銷毀過濾器
    }
}
