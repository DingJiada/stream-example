package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.LogLogin;
import com.shouzhi.pojo.db.SysRole;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 日志登录表业务层接口
 * @author WX
 * @date 2020-06-08 10:32:00
 */
public interface ILogLoginService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(LogLogin record) throws Exception;

    Integer insertSelective(LogLogin record) throws Exception;

    LogLogin selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(LogLogin record) throws Exception;

    Integer updateByPrimaryKey(LogLogin record) throws Exception;

    /**
     * 根据参数查询日志登录列表
     * @param map
     * @author WX
     * @date 2020-06-08 10:32:00
     */
    List<LogLogin> queryListByPage(Map<String, Object> map);

    /**
     * 新增日志登录记录
     * @param loginState 登录状态，1成功，0失败
     * @param loginCode 状态码，具体的状态描述
     * @param createId 创建人id
     * @param createBy 创建人账号
     * @param req
     * @author WX
     * @date 2020-06-29 14:51:20
     */
    Integer save(String loginState, String loginCode, String createId, String createBy, HttpServletRequest req) throws Exception;

}
