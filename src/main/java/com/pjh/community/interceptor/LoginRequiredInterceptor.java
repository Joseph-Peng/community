package com.pjh.community.interceptor;

import com.pjh.community.annotation.LoginRequired;
import com.pjh.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求的是不是一个方法
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod) handler;
            LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
            if(loginRequired!=null && hostHolder.getUser()==null){
                response.sendRedirect(request.getContextPath()+ "/login");
                return false;
            }
        }
        return true;
    }
}
