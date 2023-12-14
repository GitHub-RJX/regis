package com.rjx.regis.filter;

import com.alibaba.fastjson.JSON;
import com.rjx.regis.common.BaseContext;
import com.rjx.regis.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // 进行路径匹配，支持通配符
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次处理请求的URL
        String requestURI = request.getRequestURI();

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //判断本次请求是否需要处理
        boolean check = this.check(urls, requestURI);

        log.info("拦截到请求：{}", requestURI);
        //若不需要，直接放行
        if (check) {
            log.info("请求无需登录：{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //4-1 判断登陆状态，若已经登陆直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            long id = Thread.currentThread().getId();
            log.info("线程ID为：{}", id);
            filterChain.doFilter(request, response);
            return;
        }

        //4-2 判断登陆状态，若已经登陆直接放行(移动端)
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，id为：{}", request.getSession().getAttribute("user"));

            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);
            long id = Thread.currentThread().getId();
            log.info("线程ID为：{}", id);
            filterChain.doFilter(request, response);
            return;
        }
        //若未登陆则返回未登陆结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 进行路径匹配，检查是否可以放行
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
