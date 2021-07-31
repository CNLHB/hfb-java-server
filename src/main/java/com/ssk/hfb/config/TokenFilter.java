package com.ssk.hfb.config;

import com.ssk.hfb.common.utils.JwtUtils;
import com.ssk.hfb.common.utils.ListUtils;
import com.ssk.hfb.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class TokenFilter implements Filter{
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        boolean isFilter = false;
        String token = request1.getHeader("Authorization");
        String servletPath = request1.getServletPath();
        String method = request1.getMethod();
        if ("OPTIONS".equals(method)){
            chain.doFilter(request, response);
            return;
        }
        if (ListUtils.contains(jwtProperties.getPath(), servletPath)){
            log.info(servletPath + "请求白名单");
            chain.doFilter(request, response);
        }else {
            if ("".equals(token)|| token == null){

                log.error("token为空");
                log.error(method);
                log.error(servletPath);
                res.sendError(401,"token已过期");
                return ;
            }else {
                boolean expiration=false;
                try{
                    expiration = JwtUtils.isExpiration(token, jwtProperties.getPublicKey());
                    // 放入线程域
                    log.info("token有效   "+ servletPath);
                }catch (Exception e){
                    log.error(e.getMessage());
                    res.sendError(401,"token已过期");
                    return;
                }

            }
            chain.doFilter(request, response);
        }





    }

    @Override
    public void destroy() {

    }

}
