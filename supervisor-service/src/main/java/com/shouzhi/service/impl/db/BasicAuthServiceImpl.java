package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.CredentialsUtil;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.BasicAuthMapper;
import com.shouzhi.pojo.db.*;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.constants.RedisConst;
import com.shouzhi.service.constants.SecretKeyConst;
import com.shouzhi.service.interf.db.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础认证表业务层接口实现类
 * @author WX
 * @date 2020-07-13 09:56:05
 */
@Service("basicAuthService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class BasicAuthServiceImpl implements IBasicAuthService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BasicAuthMapper basicAuthMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISecurityQuestionsService securityQuestionsService;

    @Autowired
    private ISecurityQuestionSetService securityQuestionSetService;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    RedisTemplateService redisTemplateService;

    @Autowired
    BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return basicAuthMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(BasicAuth record) throws Exception {
        return basicAuthMapper.insert(record);
    }

    @Override
    public Integer insertSelective(BasicAuth record) throws Exception {
        return basicAuthMapper.insertSelective(record);
    }

    @Override
    public BasicAuth selectByPrimaryKey(String id) {
        return basicAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(BasicAuth record) throws Exception {
        return basicAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(BasicAuth record) throws Exception {
        return basicAuthMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询基础认证列表
     * @param record
     * @author WX
     * @date 2020-07-13 09:36:05
     */
    @Override
    public List<BasicAuth> queryListByPage(Map<String, Object> record) {
        return basicAuthMapper.queryListByPage(record);
    }

    /**
     * 根据参数查询账号信息
     * @param record
     * @author WX
     * @date 2020-07-13 10:53:00
     */
    @Override
    public BasicAuth selectOneByParam(BasicAuth record) {
        return basicAuthMapper.selectOneByParam(record);
    }

    /**
     * 更新字段为NULL
     * @param record
     * @author WX
     * @date 2020-07-20 19:04:13
     */
    @Override
    public Integer updateWithNull(BasicAuth record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        BasicAuth basicAuth = this.selectByPrimaryKey(record.getId());
        Assert.notNull(basicAuth,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = basicAuthMapper.updateWithNull(record);
        Assert.isTrue(count==1,"更新基础认证账号失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(basicAuth), JSON.toJSONString(record));
        return count;
    }

    /**
     * 新增账号
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer save(BasicAuth record, String permId, HttpServletRequest req) throws Exception {
        // SysUser userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入基础认证账号失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, record, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新账号
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer update(BasicAuth record, String permId, HttpServletRequest req, String... operType) throws Exception {
        // 此处只能从数据库取，因为找回密码-验证密保也会跳入此方法，未登录是没有用户信息的
        BasicAuth basicAuth = this.selectByPrimaryKey(record.getId());
        Assert.notNull(basicAuth,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新基础认证账号失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, basicAuth, record.getId(), JSON.toJSONString(basicAuth), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除账号
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        BasicAuth record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }


    /**
     * 根据IDs批量删除
     * @param basicAuthIds 账户(号)信息IDs，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId          权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-23 17:54:02
     */
    @Override
    public Integer batchDelete(List<String> basicAuthIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        Assert.isTrue(CollectionUtils.isNotEmpty(basicAuthIds),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",basicAuthIds);
        map.put("idIn","1");
        List<BasicAuth> basicAuths = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(basicAuths) && basicAuths.size()==basicAuthIds.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, basicAuths, null);
        Assert.isTrue(count==basicAuths.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据ID校验账号密码
     * @param rowId        数据行ID
     * @param userName     用户名
     * @param oldPwd       旧密码
     * @param operType 操作类型：PWD(修改密码验证)、MOBILE(修改手机验证)、EMAIL(修改邮箱验证)、SECURITY(修改密保验证)、CANCEL_ACCOUNT(注销用户验证)
     * @author WX
     * @date 2020-07-15 09:57:41
     */
    @Override
    public Integer verifyPwd(String rowId, String userName, String oldPwd, String operType, HttpServletRequest req) throws Exception {
        BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        if(!userName.equals(basicAuth.getUserName()) || !CredentialsUtil.MD5Pwd(oldPwd, userName, SecretKeyConst.MD5_HASH_ITERATIONS).equals(basicAuth.getPassWord())){
            if(!userName.equals(basicAuth.getPersonNum()) || !CredentialsUtil.MD5Pwd(oldPwd, basicAuth.getSalt(), SecretKeyConst.MD5_HASH_ITERATIONS).equals(basicAuth.getPassWord())){
                throw new IllegalArgumentException("USERNAME_PWD_ERROR");
            }
        }
        String username = basicAuth.getUserName();

        // 校验通过下发密钥
        switch (operType.toUpperCase()){
            case "PWD":
                // 生成修改密码的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_PWD_KEY(rowId), username, RedisConst.TTL_SECONDS_900);
                break;
            case "MOBILE":
                // 生成修改手机的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_MOBILE_KEY(rowId), username, RedisConst.TTL_SECONDS_900);
                break;
            case "EMAIL":
                // 生成修改邮箱的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_EMAIL_KEY(rowId), username, RedisConst.TTL_SECONDS_900);
                break;
            case "SECURITY":
                // 生成设置密保的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_SECURITY_KEY(rowId), username, RedisConst.TTL_SECONDS_900);
                break;
            case "CANCEL_ACCOUNT":
                // 生成注销用户步骤一的密钥
                redisTemplateService.setStr(RedisConst.CANCEL_ACCOUNT_1_KEY(rowId), username, RedisConst.TTL_SECONDS_900);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return 1;
    }

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
    @Override
    public Integer verifySecurityQuestion(String rowId, String securityQuestionId1, String answer1, String securityQuestionId2, String answer2, String operType, HttpServletRequest req) throws Exception {
        // 此处只能从数据库取，因为找回密码-验证密保也会跳入此方法，未登录是没有用户信息的
        BasicAuth userInfo = this.selectByPrimaryKey(rowId);
        SecurityQuestionSet sqs = new SecurityQuestionSet();
        sqs.setBasicAuthId(rowId);
        sqs.setSecurityQuestionId(securityQuestionId1);
        sqs.setAnswer(answer1);
        List<SecurityQuestionSet> sqsList = securityQuestionSetService.queryListByPage(sqs);
        Assert.notEmpty(sqsList, "USER_SECURITY_QUESTION_ERROR");
        sqs.setSecurityQuestionId(securityQuestionId2);
        sqs.setAnswer(answer2);
        sqsList = securityQuestionSetService.queryListByPage(sqs);
        Assert.notEmpty(sqsList, "USER_SECURITY_QUESTION_ERROR");
        // TODO 等业务差不多了，看看这块和上边那块能不能封装起来
        // 校验通过下发密钥
        switch (operType.toUpperCase()){
            case "SECURITY":
                // 生成修改密保的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_SECURITY_KEY(rowId), userInfo.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            case "CANCEL_ACCOUNT":
                // 校验步骤一的密钥是否正确
                baseService.hasVerifyKey(RedisConst.CANCEL_ACCOUNT_1_KEY(rowId));
                // 生成注销账户步骤二的密钥
                redisTemplateService.setStr(RedisConst.CANCEL_ACCOUNT_2_KEY(rowId), userInfo.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            case "RETRIEVE_PASSWORD":
                // 生成修改密码的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_PWD_KEY(rowId), userInfo.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return 1;
    }

    /**
     * 根据ID校验邮箱
     * @param rowId     数据行ID
     * @param permId    权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuth
     * @author WX
     * @date 2020-07-17 11:12:23
     */
    @Override
    public Integer verifyMail(String rowId, String permId, BasicAuth basicAuth, String operType, HttpServletRequest req) throws Exception {
        // 校验邮箱是否匹配
        // BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        // Assert.isTrue(userEmail.equals(basicAuth.getUserEmail()), "USER_EMAIL_NOT_EXIST");
        // 校验通过下发密钥
        switch (operType.toUpperCase()){
            case "EMAIL":
                // 生成修改邮箱的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_EMAIL_KEY(rowId), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            case "CANCEL_ACCOUNT":
                // 校验步骤一的密钥是否正确
                baseService.hasVerifyKey(RedisConst.CANCEL_ACCOUNT_1_KEY(rowId));
                // 生成注销账户步骤二的密钥
                redisTemplateService.setStr(RedisConst.CANCEL_ACCOUNT_2_KEY(rowId), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return 1;
    }

    /**
     * 根据ID校验手机
     * @param rowId     数据行ID
     * @param permId    权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuth
     * @param operType
     * @author WX
     * @date 2020-07-26 13:56:40
     */
    @Override
    public Integer verifyMobile(String rowId, String permId, BasicAuth basicAuth, String operType, HttpServletRequest req) throws Exception {
        // 校验通过下发密钥
        switch (operType.toUpperCase()){
            case "MOBILE":
                // 生成修改手机的密钥
                redisTemplateService.setStr(RedisConst.UPDATE_MOBILE_KEY(rowId), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            case "CANCEL_ACCOUNT":
                // 校验步骤一的密钥是否正确
                baseService.hasVerifyKey(RedisConst.CANCEL_ACCOUNT_1_KEY(rowId));
                // 生成注销账户步骤二的密钥
                redisTemplateService.setStr(RedisConst.CANCEL_ACCOUNT_2_KEY(rowId), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return 1;
    }

    /**
     * 根据ID更新账号密码
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param newPwd 新密码
     * @author WX
     * @date 2020-06-29 10:34:20
     */
    @Override
    public Integer updatePwd(String rowId, String permId, String newPwd, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_PWD_KEY(rowId));
        return this.doUpdatePwd(rowId, newPwd, username, permId, req);
    }

    @Override
    public Integer doUpdatePwd(String rowId, String newPwd, String salt, String permId, HttpServletRequest req) throws Exception {
        // 更改密码
        BasicAuth basicAuth=new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setPassWord(CredentialsUtil.MD5Pwd(newPwd, salt, SecretKeyConst.MD5_HASH_ITERATIONS));
        Integer count = this.update(basicAuth, permId, req);
        // 密码一旦更改清除redis中对应的User信息
        redisTemplateService.delete(RedisConst.INFO_USER(salt));
        return count;
    }

    /**
     * 根据账号ID查询密保问题列表
     * @param rowId  数据行ID
     * @author WX
     * @date 2020-07-21 11:23:02
     */
    @Override
    public List<SecurityQuestions> findSecurityQuestionListByBasicAuthId(String rowId, HttpServletRequest req) {
        return securityQuestionsService.selectByBasicAuthId(rowId);
    }


    /**
     * 根据账号ID插入密保问题
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param securityQuestionSets 密保问题设置 集合
     * @author WX
     * @date 2020-07-16 11:46:52
     */
    @Override
    public Integer saveSecurityQuestionSet(String rowId, String permId, List<SecurityQuestionSet> securityQuestionSets, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_SECURITY_KEY(rowId));
        // 插入密保问题
        Integer count = securityQuestionSetService.save(rowId, permId, securityQuestionSets, req);
        return count;
    }

    /**
     * 发送邮箱验证码
     * @param rowId
     * @param userEmail
     * @param capText
     */
    @Override
    public void sendMailCode(String rowId, String userEmail, String capText, HttpServletRequest req) throws Exception {
        // 校验
        if(StringUtils.isBlank(userEmail)){
            BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
            Assert.hasText(basicAuth.getUserEmail(), "USER_EMAIL_NOT_EXIST");
            userEmail = basicAuth.getUserEmail();
        }
        baseService.sendMailCode(userEmail, capText);
    }

    /**
     * 根据ID更新邮箱
     * @param rowId     数据行ID
     * @param permId    权限ID或菜单ID(仅限于最后级别的菜单)
     * @param userEmail 邮箱
     * @param req
     * @author WX
     * @date 2020-06-29 10:34:20
     */
    @Override
    public Integer updateMail(String rowId, String permId, String userEmail, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_EMAIL_KEY(rowId));
        // 校验邮箱是否存在
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setUserEmail(userEmail);
        basicAuth = this.selectOneByParam(basicAuth);
        Assert.isNull(basicAuth, "USER_EMAIL_EXIST");

        basicAuth = new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setUserEmail(userEmail);
        Integer count = this.update(basicAuth, permId, req);
        return count;
    }

    /**
     * 根据ID移除邮箱
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-17 14:46:13
     */
    @Override
    public Integer delMail(String rowId, String permId, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_EMAIL_KEY(rowId));
        // 移除邮箱
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setUserEmail("NULL");
        Integer count = this.updateWithNull(basicAuth, permId, req);
        return count;
    }

    /**
     * 发送手机验证码
     * @param rowId
     * @param userMobile
     * @param capText
     */
    @Override
    public void sendMobileCode(String rowId, String userMobile, String capText, HttpServletRequest req) throws Exception {
        // 校验
        if(StringUtils.isBlank(userMobile)){
            BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
            Assert.hasText(basicAuth.getUserMobile(), "USER_MOBILE_NOT_EXIST");
            userMobile = basicAuth.getUserMobile();
        }
        baseService.sendMobileCode(rowId, userMobile, capText);
    }

    /**
     * 根据ID更新手机号
     * @param rowId      数据行ID
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @param userMobile 手机号
     * @author WX
     * @date 2020-07-26 12:26:41
     */
    @Override
    public Integer updateMobile(String rowId, String permId, String userMobile, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_MOBILE_KEY(rowId));
        // 校验手机是否存在
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setUserMobile(userMobile);
        basicAuth = this.selectOneByParam(basicAuth);
        Assert.isNull(basicAuth, "USER_MOBILE_EXIST");

        basicAuth = new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setUserMobile(userMobile);
        Integer count = this.update(basicAuth, permId, req);
        return count;
    }


    /**
     * 根据ID移除手机号
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-28 11:27:53
     */
    @Override
    public Integer delMobile(String rowId, String permId, HttpServletRequest req) throws Exception {
        String username = baseService.hasVerifyKey(RedisConst.UPDATE_MOBILE_KEY(rowId));
        // 移除手机号
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setUserMobile("NULL");
        Integer count = this.updateWithNull(basicAuth, permId, req);
        return count;
    }


    /**
     * 根据ID删除账号
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-13 15:05:20
     */
    @Override
    public Integer cancelAccount(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 这里只验证CANCEL_ACCOUNT_2_KEY，因为步骤是一环一环来的，只要步骤2的key通过了就说明是合法用户
        String username = baseService.hasVerifyKey(RedisConst.CANCEL_ACCOUNT_2_KEY(rowId));

        BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        this.doCancelAccount(basicAuth, permId, req);
        return 1;
    }

    // 注销用户&系统用户删除时有使用到
    @Override
    public Integer doCancelAccount(BasicAuth basicAuth, String permId, HttpServletRequest req) throws Exception {
        Integer delete = this.delete(basicAuth.getId(), permId, req);
        // 更改注册状态为已注销
        SysUser sysUser = new SysUser();
        sysUser.setId(basicAuth.getSysUserId());
        sysUser.setIsRegistered("-1");
        sysUserService.update(sysUser, permId, req);
        // 清除redis中对应的User信息
        redisTemplateService.delete(RedisConst.INFO_USER(basicAuth.getUserName()));
        return delete;
    }

    // 批量注销用户&系统用户批量删除时有使用到
    @Override
    public Integer doBatchCancelAccount(List<String> basicAuthIds, List<String> sysUserIds, List<String> redisKeys, String permId, HttpServletRequest req) throws Exception {
        this.batchDelete(basicAuthIds, permId, req);
        // 批量更改注册状态为已注销
        if(CollectionUtils.isNotEmpty(sysUserIds)){
            Map<String, Object> map = new HashMap<>();
            map.put("isRegistered","-1");
            map.put("list",sysUserIds);
            sysUserService.BatchUpdate(map);
        }
        redisTemplateService.deleteCollection(redisKeys);
        return 1;
    }



    /**
     * 注册账号
     * @param record
     * @param permId
     * @param req
     * @author WX
     * @date 2020-07-13 16:21:43
     */
    @Override
    public Integer register(BasicAuth record, String permId, HttpServletRequest req) throws Exception {
        // 注册，user信息必须提前存在，校验是否存在
        SysUser sysUser = new SysUser();
        sysUser.setPersonNum(record.getPersonNum());
        sysUser = sysUserService.selectOneByParam(sysUser);
        Assert.notNull(sysUser,"USER_NOT_FOUND");
        // 校验是否已经注册
        Map<String, Object> param = new HashMap<>();
        param.put("uNameORpNum", "uNameORpNum");
        param.put("userNameOR", record.getUserName());
        param.put("personNumOR", record.getPersonNum());
        List<BasicAuth> basicAuths = this.queryListByPage(param);
        if (!CollectionUtils.isEmpty(basicAuths)) {
            throw new IllegalArgumentException("REG_USER_EXISTS_ERROR");
        }

        record.setPassWord(CredentialsUtil.MD5Pwd(record.getPassWord(), record.getUserName(), SecretKeyConst.MD5_HASH_ITERATIONS));
        record.setSalt(record.getUserName());
        record.setSysUserId(sysUser.getId());
        Integer save = this.save(record, permId, req);
        // 更改系统用户信息为已注册
        sysUser=new SysUser();
        sysUser.setId(record.getSysUserId());
        sysUser.setIsRegistered("1");
        sysUserService.updateByPrimaryKeySelective(sysUser); // 没有token不能使用sysUserService.update()

        // 生成设置密保的密钥，因为注册成功后会跳至初始化身份页面设置密保
        redisTemplateService.setStr(RedisConst.UPDATE_SECURITY_KEY(record.getId()), record.getUserName(), RedisConst.TTL_SECONDS_900);
        return save;
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-07-30 17:13:20
     */
    @Override
    public Integer batchInsert(List<BasicAuth> list) {
        return basicAuthMapper.batchInsert(list);
    }

    /**
     * 批量新增账号
     * @param record
     * @param permId
     * @param req
     * @author WX
     * @date 2020-07-30 17:23:54
     */
    @Override
    public Integer batchSave(List<BasicAuth> record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Integer count = this.batchInsert(record);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_BASIC_AUTH, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, record);
        Assert.isTrue(count==record.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:49:28
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return basicAuthMapper.batchDelete(map);
    }


    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-07-31 09:12:06
     */
    @Override
    public List<BasicAuth> BatchSelect(Map<String, Object> map) {
        return basicAuthMapper.BatchSelect(map);
    }

    /**
     * 启用/停用基础认证账号
     * @param rowId
     * @param permId
     * @param isLocked
     * @author WX
     * @date 2020-08-01 17:25:16
     */
    @Override
    public Integer lockedBasicAuth(String rowId, String permId, String isLocked, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Assert.isTrue(!rowId.equals(userInfo.getId()),"USER_LOCKED_ONESELF_ERROR");

        BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        String username = basicAuth.getUserName();
        basicAuth=new BasicAuth();
        basicAuth.setId(rowId);
        basicAuth.setIsLocked(isLocked);
        Integer count = this.update(basicAuth, permId, req);
        if("1".equals(isLocked)){
            // 账户一旦锁定清除redis中对应的User信息，此时再进行任何操作token会失效
            redisTemplateService.delete(RedisConst.INFO_USER(username));
        }
        return count;
    }


    /**
     * 批量停用/批量启用系统账户
     * @param basicAuthIds
     * @param permId
     * @param isLocked
     * @author WX
     * @date 2020-08-02 14:48:16
     */
    @Override
    public Integer batchLockedBasicAuth(String basicAuthIds, String permId, String isLocked, HttpServletRequest req) throws Exception {
        List<String> list = Arrays.asList(basicAuthIds.split(","));
        BasicAuth userInfo = baseService.getUserInfo(req);
        Assert.isTrue(!list.contains(userInfo.getId()),"USER_LOCKED_ONESELF_ERROR");

        // 批量更新
        Map<String, Object> map = new HashMap<>();
        map.put("isLocked",isLocked);
        map.put("list",list);
        Integer batchUpdate = this.BatchUpdate(map);

        // 批量 清除redis中对应的User信息 需要 username
        if("1".equals(isLocked)){
            map = new HashMap<>();
            map.put("idIn","id");
            map.put("list",list);
            List<BasicAuth> basicAuths = this.BatchSelect(map);
            List<String> redisKeys = basicAuths.stream().map(basicAuth -> RedisConst.INFO_USER(basicAuth.getUserName())).collect(Collectors.toList());
            redisTemplateService.deleteCollection(redisKeys);
        }
        return batchUpdate;
    }

    /**
     * 批量更新
     * @param map
     * @author WX
     * @date 2020-08-02 15:39:18
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return basicAuthMapper.BatchUpdate(map);
    }

    /**
     * 根据ID重置账号密码
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 15:56:18
     */
    @Override
    public Integer resetPwd(String rowId, String permId, HttpServletRequest req) throws Exception {
        BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        return this.doUpdatePwd(rowId, basicAuth.getPersonNum(), basicAuth.getSalt(), permId, req);
    }

    /**
     * 根据ID删除账号-后台管理进入
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 16:40:25
     */
    @Override
    public Integer cancelAccountBackground(String rowId, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        BasicAuth basicAuth = this.selectByPrimaryKey(rowId);
        logger.info("用户名={}，编号={}，正在进行危险操作-注销账号，被注销用户名={} 被注销编号={}", userInfo.getUserName(),userInfo.getPersonNum(),basicAuth.getUserName(),basicAuth.getPersonNum());
        return this.doCancelAccount(basicAuth, permId, req);
    }

    /**
     * 根据ID批量删除账号-后台管理进入
     * @param basicAuthIds
     * @param permId       权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-02 17:04:26
     */
    @Override
    public Integer batchCancelAccountBackground(String basicAuthIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        logger.info("用户名={}，编号={}，正在进行危险操作-批量注销账号，basicAuthIds={}", userInfo.getUserName(),userInfo.getPersonNum(),basicAuthIds);
        List<String> list = Arrays.asList(basicAuthIds.split(","));
        Map<String, Object> map = new HashMap<>();
        map.put("idIn","id");
        map.put("list",list);
        List<BasicAuth> basicAuths = this.BatchSelect(map);
        List<String> sysUserIds = new ArrayList<>();
        List<String> redisKeys = new ArrayList<>();
        // List<String> redisKeys = basicAuths.stream().map(basicAuth -> RedisConst.INFO_USER(basicAuth.getUserName())).collect(Collectors.toList());
        basicAuths.forEach(basicAuth -> {
            sysUserIds.add(basicAuth.getSysUserId());
            redisKeys.add(RedisConst.INFO_USER(basicAuth.getUserName()));
        });
        return this.doBatchCancelAccount(list,sysUserIds,redisKeys,permId,req);
    }
}
