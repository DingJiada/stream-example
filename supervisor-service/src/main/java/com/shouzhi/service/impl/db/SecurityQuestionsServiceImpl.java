package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SecurityQuestionsMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestions;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISecurityQuestionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 密保问题表业务层接口实现类
 * @author WX
 * @date 2020-07-15 17:28:57
 */
@Service("securityQuestionsService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SecurityQuestionsServiceImpl implements ISecurityQuestionsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityQuestionsMapper securityQuestionsMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return securityQuestionsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SecurityQuestions record) throws Exception {
        return securityQuestionsMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SecurityQuestions record) throws Exception {
        return securityQuestionsMapper.insertSelective(record);
    }

    @Override
    public SecurityQuestions selectByPrimaryKey(String id) {
        return securityQuestionsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SecurityQuestions record) throws Exception {
        return securityQuestionsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SecurityQuestions record) throws Exception {
        return securityQuestionsMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询密保问题列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:15:03
     */
    @Override
    public List<SecurityQuestions> queryListByPage(SecurityQuestions record) {
        return securityQuestionsMapper.queryListByPage(record);
    }

    /**
     * 根据认证账号ID查询密保列表
     * @param basicAuthId
     * @author WX
     * @date 2020-07-21 11:14:21
     */
    @Override
    public List<SecurityQuestions> selectByBasicAuthId(String basicAuthId) {
        return securityQuestionsMapper.selectByBasicAuthId(basicAuthId);
    }

    /**
     * 新增密保问题
     * @param record
     * @param permId
     * @param req
     * @author WX
     * @date 2020-07-16 09:51:21
     */
    @Override
    public Integer save(SecurityQuestions record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入密保问题失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SECURITY_QUESTIONS, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新密保问题
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @param operType
     * @author WX
     * @date 2020-07-16 09:52:13
     */
    @Override
    public Integer update(SecurityQuestions record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SecurityQuestions sq = this.selectByPrimaryKey(record.getId());
        Assert.notNull(sq,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新密保问题失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SECURITY_QUESTIONS, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(sq), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除密保问题
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2020-07-16 10:05:24
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除密保问题
        BasicAuth userInfo = baseService.getUserInfo(req);
        SecurityQuestions record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志(涉及到级联的操作，此日志操作一定要在级联操作前)
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SECURITY_QUESTIONS, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }
}
