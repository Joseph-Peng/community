package com.pjh.community.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

    /**
     * 在Controller执行之前执行
     * @param request
     * @param response
     * @param handler
     * @return 如果返回值为false则表示不会继续执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle" + handler.toString());
        return true;
    }

    /**
     *  在Controller执行之后执行   访问controller之后 访问视图之前被调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("postHandle" + handler.toString());
    }

    // 在TemplateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("afterCompletion" + handler.toString());
    }
}
