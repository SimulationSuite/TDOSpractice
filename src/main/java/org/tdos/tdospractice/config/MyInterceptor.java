package org.tdos.tdospractice.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.tdos.tdospractice.service.SecurityService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Resource
    private SecurityService securityService;

    private static MyInterceptor myInterceptor;

    @PostConstruct
    public void init(){
        myInterceptor = this;
        myInterceptor.securityService = this.securityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new Exception("用户未登录");
        }
        myInterceptor.securityService.getUserId(authHeader);
        return true;
    }
}
