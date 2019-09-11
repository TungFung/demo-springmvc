package com.example.config;

import com.example.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.controller", "com.example.interceptor"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private MyInterceptor myInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }

    /**
     * 添加文件上传解析器
     * 文件上传解析器有两种，一种是Servlet容器提供支持的StandardServletMultipartResolver(首选),
     * 另一种是Jakarta Commons FileUpload multipart解析器
     */
    @Bean
    public MultipartResolver multipartResolver() throws IOException {
        return new StandardServletMultipartResolver();
    }
}
