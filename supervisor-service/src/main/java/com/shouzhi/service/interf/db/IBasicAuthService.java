package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestionSet;
import com.shouzhi.pojo.db.SecurityQuestions;
import com.shouzhi.pojo.db.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 基础认证表业务层接口
 * @author WX
 * @date 2020-07-13 09:56:05
 */
public interface IBasicAuthService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(BasicAuth record) throws Exception;

    Integer insertSelective(BasicAuth record) throws Exception;

    BasicAuth selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(BasicAuth record) throws Exception;

    Integer updateByPrimaryKey(BasicAuth record) throws Exception;

    /**
     * 根据参数查询基础认证列表
     * @param record
     * @author WX
     * @date 2020-07-13 09:36:05
     */
    List<BasicAuth> queryListByPage(Map<String, Object> record);

    /**
     * 根据参数查询账号信息
     * @param record
     * @author WX
     * @date 2020-07-13 10:53:00
     */
    BasicAuth selectOneByParam(BasicAuth record);

    /**
     * 更新字段为NULL
     * @param record
     * @author WX
     * @date 2020-07-20 19:04:13
     */
    Integer updateWithNull(BasicAuth record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 新增账号
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer save(BasicAuth record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新账号
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer update(BasicAuth record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除账号
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据IDs批量删除
     * @param basicAuthIds 账户(号)信息IDs，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId          权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-23 17:54:02
     */
    Integer batchDelete(List<String> basicAuthIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID校验账号密码
     * @param rowId 被操作记录行ID
     * @param userName 用户名
     * @param oldPwd 旧密码
     * @param operType 操作类型：PWD(修改密码验证)、MOBILE(修改手机验证)、EMAIL(修改邮箱验证)、SECURITY(修改密保验证)、CANCEL_ACCOUNT(注销用户验证)
     * @author WX
     * @date 2020-07-15 09:57:41
     */
    Integer verifyPwd(String rowId, String userName, String oldPwd, String operType, HttpServletRequest req) throws Exception;


    /**
     * 根据ID校验密保问题
     * @param rowId 被操作记录行ID
     * @param securityQuestionId1 密保问题1ID
     * @param answer1 密保答案1
     * @param securityQuestionId2 密保问题2ID
     * @param answer2 密保答案2
     * @param operType 操作类型：SECURITY(修改密保验证)、CANCEL_ACCOUNT(注销用户验证)、RETRIEVE_PASSWORD(找回密码验证)
     * @author WX
     * @date 2020-07-16 15:21:03
     */
    Integer verifySecurityQuestion(String rowId, String securityQuestionId1, String answer1, String securityQuestionId2, String answer2, String operType, HttpServletRequest req) throws Exception;


    /**
     * 根据ID校验邮箱
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuth
     * @author WX
     * @date 2020-07-17 11:12:23
     */
    Integer verifyMail(String rowId, String permId, BasicAuth basicAuth, String operType, HttpServletRequest req) throws Exception;


    /**
     * 根据ID校验手机
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuth
     * @author WX
     * @date 2020-07-26 13:56:40
     */
    Integer verifyMobile(String rowId, String permId, BasicAuth basicAuth, String operType, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新账号密码
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param newPwd 新密码
     * @author WX
     * @date 2020-06-29 10:34:20
     */
    Integer updatePwd(String rowId, String permId, String newPwd, HttpServletRequest req) throws Exception;
    Integer doUpdatePwd(String rowId, String newPwd, String salt, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据账号ID查询密保问题列表
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-07-21 11:23:02
     */
    List<SecurityQuestions> findSecurityQuestionListByBasicAuthId(String rowId, HttpServletRequest req);


    /**
     * 根据账号ID插入密保问题
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param securityQuestionSets 密保问题设置 集合
     * @author WX
     * @date 2020-07-16 11:46:52
     */
    Integer saveSecurityQuestionSet(String rowId, String permId, List<SecurityQuestionSet> securityQuestionSets, HttpServletRequest req) throws Exception;

    /**
     * 发送邮箱验证码
     * @param rowId
     * @param userEmail
     */
    void sendMailCode(String rowId, String userEmail, String capText, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新邮箱
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param userEmail 邮箱
     * @author WX
     * @date 2020-06-29 10:34:20
     */
    Integer updateMail(String rowId, String permId, String userEmail, HttpServletRequest req) throws Exception;


    /**
     * 根据ID移除邮箱
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-17 14:46:13
     */
    Integer delMail(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 发送手机验证码
     * @param rowId
     * @param userMobile
     */
    void sendMobileCode(String rowId, String userMobile, String capText, HttpServletRequest req) throws Exception;


    /**
     * 根据ID更新手机号
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param userMobile 手机号
     * @author WX
     * @date 2020-07-26 12:26:41
     */
    Integer updateMobile(String rowId, String permId, String userMobile, HttpServletRequest req) throws Exception;


    /**
     * 根据ID移除手机号
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-28 11:27:53
     */
    Integer delMobile(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID删除账号
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-13 15:05:20
     */
    Integer cancelAccount(String rowId, String permId, HttpServletRequest req) throws Exception;

    Integer doCancelAccount(BasicAuth basicAuth, String permId, HttpServletRequest req) throws Exception;


    Integer doBatchCancelAccount(List<String> basicAuthIds, List<String> sysUserIds, List<String> redisKeys, String permId, HttpServletRequest req) throws Exception;


    /**
     * 注册账号
     * @param record
     * @param permId
     * @author WX
     * @date 2020-07-13 16:21:43
     */
    Integer register(BasicAuth record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-07-30 17:13:20
     */
    Integer batchInsert(List<BasicAuth> list);

    /**
     * 批量新增账号
     * @param record
     * @param permId
     * @author WX
     * @date 2020-07-30 17:23:54
     */
    Integer batchSave(List<BasicAuth> record, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:49:28
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;


    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-07-31 09:12:06
     */
    List<BasicAuth> BatchSelect(Map<String, Object> map);

    /**
     * 启用/停用基础认证账号
     * @param rowId
     * @param permId
     * @param isLocked
     * @author WX
     * @date 2020-08-01 17:25:16
     */
    Integer lockedBasicAuth(String rowId, String permId, String isLocked, HttpServletRequest req) throws Exception;


    /**
     * 批量停用/批量启用系统账户
     * @param basicAuthIds
     * @param permId
     * @param isLocked
     * @author WX
     * @date 2020-08-02 14:48:16
     */
    Integer batchLockedBasicAuth(String basicAuthIds, String permId, String isLocked, HttpServletRequest req) throws Exception;


    /**
     * 批量更新
     * @param map
     * @author WX
     * @date 2020-08-02 15:39:18
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 根据ID重置账号密码
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 15:56:18
     */
    Integer resetPwd(String rowId, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID删除账号-后台管理进入
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 16:40:25
     */
    Integer cancelAccountBackground(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据ID批量删除账号-后台管理进入
     * @param basicAuthIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 17:04:26
     */
    Integer batchCancelAccountBackground(String basicAuthIds, String permId, HttpServletRequest req) throws Exception;

}
