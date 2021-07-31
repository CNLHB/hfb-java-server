package com.ssk.hfb.Interceptor;

import com.ssk.hfb.common.pojo.UserInfo;
import com.ssk.hfb.common.utils.JwtUtils;
import com.ssk.hfb.common.utils.ListUtils;
import com.ssk.hfb.config.JwtProperties;
import com.ssk.hfb.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        try {
            String servletPath = request.getServletPath();
            String method = request.getMethod();
            String token = request.getHeader("Authorization");
            log.info(servletPath);
            if ("OPTIONS".equals(method)){
                return Boolean.TRUE;
            }
            if (ListUtils.contains(jwtProperties.getPath(), servletPath)){
                log.info(servletPath + "请求白名单");
                if ("/user/verify".equals(servletPath)){
                    return Boolean.TRUE;
                }
                try{
                    UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());;//从请求认证服务获取用户信息
                    UserContext.set(user);
                    // 放入线程域
                }catch (Exception e){
                    log.error("过滤器token解析出错" + e.getMessage());
                }

                return Boolean.TRUE;
            }
            if ( StringUtils.isBlank(token)){
                log.error(method + " " + servletPath + " token为空或为null");
                // 未登录,返回401
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return Boolean.FALSE;
            }
            try{
                log.info(method + " " + servletPath );
                UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());;//从请求认证服务获取用户信息
                UserContext.set(user);
                // 放入线程域
            }catch (Exception e){
                log.error("二次token解析出错" + e.getMessage());
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("拦截器出错", e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 请求处理之后进行调用（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv)
            throws Exception {

    }

    /**
     * 在整个请求结束之后被调用（主要是用于进行资源清理工作）
     * 一定要在请求结束后调用remove清除当前线程的副本变量值，否则会造成内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
            throws Exception {
        UserContext.remove();
    }

}
