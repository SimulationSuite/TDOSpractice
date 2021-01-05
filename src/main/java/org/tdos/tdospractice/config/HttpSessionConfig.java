package org.tdos.tdospractice.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HttpSessionConfig implements WebMvcConfigurer {

    @Bean
    public ServletListenerRegistrationBean getListener(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new TDOSSessionListener());
        return servletListenerRegistrationBean;
    }
}
