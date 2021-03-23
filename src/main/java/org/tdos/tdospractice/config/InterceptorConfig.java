package org.tdos.tdospractice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig  implements WebMvcConfigurer {
    @Autowired
    private MyInterceptor myInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor());
        //排除的路径
        registration.excludePathPatterns("/login");
        //将这个controller放行
        registration.excludePathPatterns("/createToken");
        //将这个controller放行
        registration.excludePathPatterns("/download_excel");
        //将这个controller放行
        registration.excludePathPatterns("/download_qb_excel");
        //将这个controller放行
        registration.excludePathPatterns("/upload_excel");
        //将这个controller放行
        registration.excludePathPatterns("/upload_qb_excel");
        //拦截全部
        registration.addPathPatterns("/**");
    }

    @Bean
    MyInterceptor loginInterceptor() {
        return new MyInterceptor();
    }
}
