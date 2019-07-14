package cn.com.jgyhw.sqm.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "loginFilter")
public class LoginFilter implements Filter {

    private static Logger LOGGER = LogManager.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("开始登陆验证-->");
        // 获得在下面代码中要用的request,response,session对象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();

        //获取url参数，例如：v=1535632783180&id=7
        String queryString = servletRequest.getQueryString();
        //全路径url，例如：http://localhost:8080/test/test.jsp
        String requestUrl = servletRequest.getRequestURL().toString();
        //不含项目路径url，例如：/test/test.jsp
        String requestUri = servletRequest.getRequestURI();
        LOGGER.info("登陆验证URL-->" + requestUrl + "；参数：" + queryString);
        if(requestUri.indexOf("XtglAuth/") != -1){//登陆页
            LOGGER.info("系统管理登陆验证");
            String userName = (String)session.getAttribute("xtglUserName");
            LOGGER.info("session用户名：" + userName);
            if(StringUtils.isBlank(userName)){//没有，重定向到系统管理登陆页面
                LOGGER.info("系统管理登陆验证，跳转-->");
                servletResponse.sendRedirect("/manage");
                return;
            }else{
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }else if(requestUri.indexOf("pcAuthority/") != -1){
            //验证session有没有登陆信息
            String u = (String)session.getAttribute("webUser");
            if(StringUtils.isBlank(u)){//没有，重定向到Web登陆页面
                LOGGER.info("WEB验证不通过，跳转-->");
                servletResponse.sendRedirect("/");
                return;
            }else{//有继续请求
                LOGGER.info("WEB验证通过继续请求-->");
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }else{
            LOGGER.info("无需验证");
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
