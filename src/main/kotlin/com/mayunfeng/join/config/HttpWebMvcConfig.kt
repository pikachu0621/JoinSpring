package com.mayunfeng.join.config

import com.mayunfeng.join.base.BaseCls
import com.mayunfeng.join.interceptor.HttpWebInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.BufferedImageHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import javax.annotation.Resource


@Configuration
class HttpWebMvcConfig : WebMvcConfigurer , BaseCls() {


    @Autowired
    private lateinit var httpWebInterceptor: HttpWebInterceptor

    @Autowired
    private lateinit var APPConfig: AppConfig

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(httpWebInterceptor) // 注册拦截器
            .addPathPatterns("/**")     // 拦截所有请求，包括静态资源文件
            .excludePathPatterns(
                "/myf-user-api/login-user",
                "/myf-bg-api/login",
                "/myf-puc-api/**",
                "/admin/**",
                "/*.html",
                "/css/**",
                "/images/**",
                "/image/**",
                "/js/**",
                "/error/**",
                "/fonts/**")  //登陆API，静态资源
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/admin/").setViewName("forward:/admin/index.html")
    }

    // 添加静态资源路径
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 开放用户静态资源  会走拦截器
        //  registry.addResourceHandler("${APPConfig.configUserStatic}/**").addResourceLocations("file:${APPConfig.configUserStaticFilePath()}")
    }

    // 图片转换器   扩展消息转换器
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(BufferedImageHttpMessageConverter())
    }
}