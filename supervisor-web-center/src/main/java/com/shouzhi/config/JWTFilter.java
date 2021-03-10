package com.shouzhi.config;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WX
 * @date 2019-11-17 13:53:46
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String TOKEN_HEADER_KEY = "Authorization"; //Bearer
    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        //判断请求的请求头是否带上 "token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                this.executeLogin(request, response);
                return true;
            } catch (Exception e) {
                CommonResult<String> cr = new CommonResult<>();
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof RuntimeException) {
                    switch (throwable.getMessage()){
                        // access_token失效
                        case "1102001":
                            cr.setErrorResult(ErrorCodeEnum.TOKEN_A_EXPIRED_ERROR);
                            break;
                        // refresh_token失效
                        case "1102004":
                            cr.setErrorResult(ErrorCodeEnum.TOKEN_R_EXPIRED_ERROR);
                            break;
                        // token认证失败
                        case "1102002":
                            cr.setErrorResult(ErrorCodeEnum.TOKEN_AUTC_ERROR);
                            break;
                        // 密码或用户信息被重置,token失效
                        case "1102003":
                            cr.setErrorResult(ErrorCodeEnum.TOKEN_EXPIRED_RESET_ERROR);
                            break;
                    }
                }else {
                    cr.setErrorResult(ErrorCodeEnum.TOKEN_AUTC_ERROR);
                }
                this.responseError(response, JSON.toJSONString(cr));
                return false;
            }
        }else {
            CommonResult<String> cr = new CommonResult<>();
            cr.setErrorResult(ErrorCodeEnum.TOKEN_AUTC_ERROR);
            this.responseError(response, JSON.toJSONString(cr));
        }
        // 如果请求头不存在token,直接返回false,不存在网上普遍说的登录或游客访问,任何匿名URL一律在ShiroConfig配置(若返回true,任何一个需要验证的url只要不携带token都将被放行)
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN_HEADER_KEY);
        return token != null;
    }

    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN_HEADER_KEY);
        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行登入，如果错误它会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * isAccessAllowed()返回false,调用此方法
     * 直接返回false,否则仍会继续调用
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) {
        return false;
    }

    /**
     * 输出错误信息
     */
    private void responseError(ServletResponse response, String message) {
        PrintWriter out = null;
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            // httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json; charset=utf-8");
            out = httpServletResponse.getWriter();
            out.write(message);
            out.flush();
        }catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
