package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SecurityQuestions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 密保问题表业务层接口
 * @author WX
 * @date 2020-07-15 17:27:04
 */
public interface ISecurityQuestionsService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SecurityQuestions record) throws Exception;

    Integer insertSelective(SecurityQuestions record) throws Exception;

    SecurityQuestions selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SecurityQuestions record) throws Exception;

    Integer updateByPrimaryKey(SecurityQuestions record) throws Exception;

    /**
     * 根据参数查询密保问题列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:15:03
     */
    List<SecurityQuestions> queryListByPage(SecurityQuestions record);


    /**
     * 根据认证账号ID查询密保列表
     * @param basicAuthId
     * @author WX
     * @date 2020-07-21 11:14:21
     */
    List<SecurityQuestions> selectByBasicAuthId(String basicAuthId);


    /**
     * 新增密保问题
     * @param record
     * @param permId
     * @author WX
     * @date 2020-07-16 09:51:21
     */
    Integer save(SecurityQuestions record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新密保问题
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-16 09:52:13
     */
    Integer update(SecurityQuestions record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除密保问题
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-16 10:05:24
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

}
