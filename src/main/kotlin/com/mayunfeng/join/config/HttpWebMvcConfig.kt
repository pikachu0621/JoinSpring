package com.mayunfeng.join.config

import com.mayunfeng.join.BaseCls
import com.mayunfeng.join.interceptor.HttpWebInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource


@Configuration
class HttpWebMvcConfig : WebMvcConfigurer , BaseCls() {


    @Autowired
    private lateinit var httpWebInterceptor: HttpWebInterceptor


    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(httpWebInterceptor) // 注册拦截器
            .addPathPatterns("/**")     // 拦截所有请求，包括静态资源文件
            .excludePathPatterns(
                "/myf-user-api/login-user",
                "/myf-test-api/**",
                "/*.html",
                "/css/**",
                "/images/**",
                "/image/**",
                "/js/**",
                "/error/**",
                "/fonts/**")  //登陆API，静态资源
    }


}