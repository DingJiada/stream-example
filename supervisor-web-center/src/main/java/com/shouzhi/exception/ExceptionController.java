package com.shouzhi.exception;

import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import org.apache.shiro.ShiroException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller层异常统一处理
 * @author WX
 * @date 2019-11-06 00:27:28
 */
@ControllerAdvice
// @RestControllerAdvice
public class ExceptionController {

    /*@ExceptionHandler(org.apache.shiro.authz.AuthorizationException.class)
    @ResponseBody
    public String handleException(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("message", "抱歉！您没有权限执行这个操作，请联系管理员！");
        String url = WebUtils.getRequestUri(request);
        return "redirect:/" + url.split("/")[1];    // 请求的规则 : /page/operate
    }*/

    @ExceptionHandler(org.apache.shiro.authz.AuthorizationException.class)
    @ResponseBody
    public CommonResult<String> authorizationExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_SHIRO_AUTHORIZATION_ERROR);
        return cr;
    }

    @ExceptionHandler(org.apache.shiro.authz.UnauthorizedException.class)
    @ResponseBody
    public CommonResult<String> unauthorizedExceptionhandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_SHIRO_UNAUTHORIZED_ERROR);
        return cr;
    }

    @ExceptionHandler(org.apache.shiro.authc.AuthenticationException.class)
    @ResponseBody
    public CommonResult<String> authenticationExceptionException(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_SHIRO_AUTHENTICATION_ERROR);
        return cr;
    }

    // 捕捉shiro的异常 401
    @ResponseBody
    @ExceptionHandler(ShiroException.class)
    public CommonResult<String> ShiroExceptionHandle() {
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_SHIRO_401_ERROR);
        return cr;
    }

    @ResponseBody
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public CommonResult<String> missingServletRequestParameterExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        //TODO 项目上线后需将返回前端的异常详情，小括号部分删除
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_MISSING_SERVLET_REQUEST_PARAMETER_ERROR, "("+exception.getMessage()+")");
        return cr;
    }

    @ResponseBody
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public CommonResult<String> httpRequestMethodNotSupportedExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        //TODO 项目上线后需将返回前端的异常详情，小括号部分删除
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR, "("+exception.getMessage()+")");
        return cr;
    }

    @ResponseBody
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public CommonResult<String> httpMediaTypeNotSupportedExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        //TODO 项目上线后需将返回前端的异常详情，小括号部分删除
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR, "("+exception.getMessage()+")");
        return cr;
    }


    @ResponseBody
    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public CommonResult<String> multipartExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_MULTIPART_ERROR);
        return cr;
    }

    @ResponseBody
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public CommonResult<String> httpMessageNotReadableExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_HTTP_MESSAGE_NOT_READABLE_ERROR);
        return cr;
    }

    @ResponseBody
    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    public CommonResult<String> SQLIntegrityConstraintViolationExceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_SQL_INTEGRITY_CONSTRAINT_VIOLATION_ERROR);
        return cr;
    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResult<String> exceptionHandle(RedirectAttributes redirectAttributes, Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        CommonResult<String> cr = new CommonResult<>();
        cr.setErrorResult(ErrorCodeEnum.EXCEPTION_EXCEPTION_ERROR);
        return cr;
    }

}
