package com.shouzhi.controller;

import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.constants.DBEnum;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BaseController
 * @author WX
 * @date 2020-06-09 10:07:11
 */
@Component
public class BaseController {

    /**
     * 为什么不直接返回PageInfo而又重新封装一层，
     * 因为PageInfo的注释书写不规范、被打成jar包后注释被抹除、最主要还是config.setApiObjectReplacements没有生效
     * @param pageInfo
     * @param <T>
     * @return
     */
    public <T> PageInfoVo<T> filterPage(PageInfo<T> pageInfo){
        PageInfoVo<T> instance = PageInfoVo.<T>getInstance();
        instance.setPageNum(pageInfo.getPageNum())
                .setPageSize(pageInfo.getPageSize())
                .setPages(pageInfo.getPages())
                .setNavigateLastPage(pageInfo.getNavigateLastPage())
                .setNavigateFirstPage(pageInfo.getNavigateFirstPage())
                .setTotal(pageInfo.getTotal())
                .setList(pageInfo.getList());
        return instance;
    }

    /**
     * New function code block please write here ~ ~ (Don't delete this prompt)
     */

    /**
     * 填充非法参数异常错误结果
     * @description ！由于本代码块比较长，为了容易阅读，建议将新功能代码块添加在此块上方，请不要在此代码块以下再添加代码！
     * @param result 返回结果对象
     * @param e 异常对象
     * @param printStackTrace 是否打印异常堆栈
     * @param printLogger 是否打印日志
     * @param logger 日志对象
     * @param <T>
     */
    public <T> void fillIllegalArgResult(CommonResult<T> result, Exception e, boolean printStackTrace, boolean printLogger, Logger logger) {
        if(!"java.lang.IllegalArgumentException".equals(e.getClass().getCanonicalName())){
            this.fillErrorResult(result, e, printStackTrace, printLogger, logger);
            return;
        }
        if(printStackTrace) e.printStackTrace();
        if(printLogger) logger.error(e.getMessage());
        switch (e.getMessage()){
            case "COMMON_INVALID_ARG_ERROR":
                result.setErrorResult(ErrorCodeEnum.COMMON_INVALID_ARG_ERROR);
                break;

            case "USER_NOT_FOUND":
                result.setErrorResult(ErrorCodeEnum.USER_NOT_FOUND);
                break;
            case "USER_LOCKED_ONESELF_ERROR":
                result.setErrorResult(ErrorCodeEnum.USER_LOCKED_ONESELF_ERROR);
                break;
            case "USERNAME_PWD_ERROR":
                result.setErrorResult(ErrorCodeEnum.USERNAME_PWD_ERROR);
                break;
            case "USER_SECURITY_QUESTION_ERROR":
                result.setErrorResult(ErrorCodeEnum.USER_SECURITY_QUESTION_ERROR);
                break;
            case "USER_EMAIL_NOT_EXIST":
                result.setErrorResult(ErrorCodeEnum.USER_EMAIL_NOT_EXIST);
                break;
            case "USER_EMAIL_EXIST":
                result.setErrorResult(ErrorCodeEnum.USER_EMAIL_EXIST);
                break;
            case "USER_MOBILE_NOT_EXIST":
                result.setErrorResult(ErrorCodeEnum.USER_MOBILE_NOT_EXIST);
                break;
            case "USER_MOBILE_EXIST":
                result.setErrorResult(ErrorCodeEnum.USER_MOBILE_EXIST);
                break;
            case "USER_MOBILE_SEND_ERROR":
                result.setErrorResult(ErrorCodeEnum.USER_MOBILE_SEND_ERROR);
                break;
            case "REG_USER_EXISTS_ERROR":
                result.setErrorResult(ErrorCodeEnum.REG_USER_EXISTS_ERROR);
                break;
            case "REG_USER_LIST_EXISTS_ERROR":
                result.setErrorResult(ErrorCodeEnum.REG_USER_LIST_EXISTS_ERROR);
                break;

            case "FILE_UPLOAD_EMPTY_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_UPLOAD_EMPTY_ERROR);
                break;
            case "FILE_UPLOAD_TYPE_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_UPLOAD_TYPE_ERROR);
                break;
            case "FILE_NOT_EXIST_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_NOT_EXIST_ERROR);
                break;
            case "FILE_UPLOAD_INVALID_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_UPLOAD_INVALID_ERROR);
                break;
            case "FILE_UPLOAD_AUTH_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_UPLOAD_AUTH_ERROR);
                break;
            case "FILE_IMPORT_FAIL_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_IMPORT_FAIL_ERROR);
                break;
            case "FILE_EXPORT_FAIL_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_EXPORT_FAIL_ERROR);
                break;
            case "FILE_DOWNLOAD_FAIL_ERROR":
                result.setErrorResult(ErrorCodeEnum.FILE_DOWNLOAD_FAIL_ERROR);
                break;

            case "EXAM_VIDEO_LOGIN_TIME_NULL_ERROR":
                result.setErrorResult(ErrorCodeEnum.EXAM_VIDEO_LOGIN_TIME_NULL_ERROR);
                break;

            case "SCH_SPACE_CODE_INVALID_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SPACE_CODE_INVALID_ERROR);
                break;
            case "SCH_SPACE_CODE_EXIST_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SPACE_CODE_EXIST_ERROR);
                break;
            case "SCH_SPACE_TYPE_NOT_EQUAL_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SPACE_TYPE_NOT_EQUAL_ERROR);
                break;
            case "SCH_SPACE_SELECTED_PARENT_SPACE_NOT_INSERT_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SPACE_SELECTED_PARENT_SPACE_NOT_INSERT_ERROR);
                break;

            case "TREE_NOT_SELECTED_PARENT_NODE_ERROR":
                result.setErrorResult(ErrorCodeEnum.TREE_NOT_SELECTED_PARENT_NODE_ERROR);
                break;

            case "ONVIF_INITIAL_DEVICES_CONNECT_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_INITIAL_DEVICES_CONNECT_ERROR);
                break;
            case "ONVIF_PTZ_NO_SUPPORT_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_PTZ_NO_SUPPORT_ERROR);
                break;
            case "ONVIF_PTZ_ABSOLUTE_MOVE_NO_SUPPORT_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_PTZ_ABSOLUTE_MOVE_NO_SUPPORT_ERROR);
                break;
            case "ONVIF_PTZ_CONTINUOUS_MOVE_NO_SUPPORT_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_PTZ_CONTINUOUS_MOVE_NO_SUPPORT_ERROR);
                break;
            case "ONVIF_USER_OR_PWD_NULL_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_USER_OR_PWD_NULL_ERROR);
                break;
            case "ONVIF_GET_PROFILES_FAIL_ERROR":
                result.setErrorResult(ErrorCodeEnum.ONVIF_GET_PROFILES_FAIL_ERROR);
                break;

            case "SCH_DEVICE_SELECTED_SPACE_NOT_INSERT_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_DEVICE_SELECTED_SPACE_NOT_INSERT_ERROR);
                break;
            case "SCH_DEVICE_TYPE_QUANTITY_REACHED_LIMIT_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_DEVICE_TYPE_QUANTITY_REACHED_LIMIT_ERROR);
                break;
            case "SCH_DEVICE_NOT_FOUND_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_DEVICE_NOT_FOUND_ERROR);
                break;

            case "STATIC_PARAM_SHOW_TYPE_MISMATCH_ERROR":
                result.setErrorResult(ErrorCodeEnum.STATIC_PARAM_SHOW_TYPE_MISMATCH_ERROR);
                break;

            case "SYS_PERM_TYPE_NOT_EQUAL_ERROR":
                result.setErrorResult(ErrorCodeEnum.SYS_PERM_TYPE_NOT_EQUAL_ERROR);
                break;

            case "SYS_DEP_TYPE_NOT_EQUAL_ERROR":
                result.setErrorResult(ErrorCodeEnum.SYS_DEP_TYPE_NOT_EQUAL_ERROR);
                break;
            case "SYS_DEP_SELECTED_PARENT_DEP_NOT_INSERT_ERROR":
                result.setErrorResult(ErrorCodeEnum.SYS_DEP_SELECTED_PARENT_DEP_NOT_INSERT_ERROR);
                break;

            case "SCH_SEMESTER_TOTAL_WEEKS_NOT_EQUAL_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SEMESTER_TOTAL_WEEKS_NOT_EQUAL_ERROR);
                break;
            case "SCH_SEMESTER_ALREADY_IS_CURRENT_SEM_ERROR":
                result.setErrorResult(ErrorCodeEnum.SCH_SEMESTER_ALREADY_IS_CURRENT_SEM_ERROR);
                break;

            case "DB_SQL_INSERT_ERROR":
                result.setErrorResult(ErrorCodeEnum.DB_SQL_INSERT_ERROR);
                break;
            case "DB_SQL_DELETE_ERROR":
                result.setErrorResult(ErrorCodeEnum.DB_SQL_DELETE_ERROR);
                break;
            case "DB_SQL_UPDATE_ERROR":
                result.setErrorResult(ErrorCodeEnum.DB_SQL_UPDATE_ERROR);
                break;
            case "DB_SQL_ID_INVALID_ERROR":
                result.setErrorResult(ErrorCodeEnum.DB_SQL_ID_INVALID_ERROR);
                break;

            case "EXCEPTION_OPERATE_ERROR":
                result.setErrorResult(ErrorCodeEnum.EXCEPTION_OPERATE_ERROR);
                break;
            case "EXCEPTION_EXCEPTION_ERROR":
                result.setErrorResult(ErrorCodeEnum.EXCEPTION_EXCEPTION_ERROR);
                break;

            default:
                result.setErrorResult(ErrorCodeEnum.EXCEPTION_OPERATE_ERROR);
        }
    }


    /**
     * 填充错误结果，捕捉特定类
     * @description ！由于本代码块比较长，为了容易阅读，建议将新功能代码块添加在此块上方，请不要在此代码块以下再添加代码！
     * @param result 返回结果对象
     * @param e 异常对象
     * @param printStackTrace 是否打印异常堆栈
     * @param printLogger 是否打印日志
     * @param logger 日志对象
     * @param <T>
     */
    public <T> void fillErrorResult(CommonResult<T> result, Exception e, boolean printStackTrace, boolean printLogger, Logger logger) {
        if(printStackTrace) e.printStackTrace();
        if(printLogger) logger.error(e.getMessage());
        switch (e.getClass().getCanonicalName()){
            case "org.springframework.dao.DuplicateKeyException":
                String exceptionMessage = this.getExceptionMessage(e);
                Pattern pattern = Pattern.compile("Duplicate entry '(.*?)' for key '(.*?)'");
                Matcher matcher = pattern.matcher(exceptionMessage);
                String concatStr = "";
                if(matcher.find() && matcher.groupCount()==2){
                    concatStr = "（"+DBEnum.valueOf(matcher.group(2)).getDesc()+":"+matcher.group(1)+"）";
                }
                result.setErrorResult(ErrorCodeEnum.DB_INDEX_UNIQUE_ERROR, concatStr);
                break;

            case "com.shouzhi.service.customexception.FileImportException":
                result.setErrorResult(ErrorCodeEnum.FILE_IMPORT_FAIL_ERROR, "（"+this.getExceptionMessage(e)+"）");
                break;

            case "com.shouzhi.service.customexception.FileDownloadException":
                result.setErrorResult(ErrorCodeEnum.FILE_DOWNLOAD_FAIL_ERROR, "（"+this.getExceptionMessage(e)+"）");
                break;

            default:
                result.setErrorResult(ErrorCodeEnum.EXCEPTION_EXCEPTION_ERROR);
        }
    }

    public String getExceptionMessage(Exception e){
        return e.getCause()==null ? e.getMessage() : e.getCause().getMessage();
    }
}
