package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SecurityQuestionSetMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ExamVideo;
import com.shouzhi.pojo.db.SecurityQuestionSet;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.ConsumerWrapper;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISecurityQuestionSetService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密保问题设置表业务层接口实现类
 * @author WX
 * @date 2020-07-15 17:49:56
 */
@Service("securityQuestionSetService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SecurityQuestionSetServiceImpl implements ISecurityQuestionSetService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityQuestionSetMapper securityQuestionSetMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return securityQuestionSetMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SecurityQuestionSet record) throws Exception {
        return securityQuestionSetMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SecurityQuestionSet record) throws Exception {
        return securityQuestionSetMapper.insertSelective(record);
    }

    @Override
    public SecurityQuestionSet selectByPrimaryKey(String id) {
        return securityQuestionSetMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SecurityQuestionSet record) throws Exception {
        return securityQuestionSetMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SecurityQuestionSet record) throws Exception {
        return securityQuestionSetMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询密保问题设置列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:42:15
     */
    @Override
    public List<SecurityQuestionSet> queryListByPage(SecurityQuestionSet record) {
        return securityQuestionSetMapper.queryListByPage(record);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 09:20:15
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return securityQuestionSetMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 09:26:37
     */
    @Override
    public List<SecurityQuestionSet> BatchSelect(Map<String, Object> map) {
        return securityQuestionSetMapper.BatchSelect(map);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-11-23 09:43:19
     */
    @Override
    public Integer batchInsert(List<SecurityQuestionSet> list) {
        return securityQuestionSetMapper.batchInsert(list);
    }

    /**
     * 新增密保问题设置
     * @param securityQuestionSets
     * @param permId
     * @author WX
     * @date 2020-07-16 10:34:06
     */
    @Override
    public Integer save(String basicAuthId, String permId, List<SecurityQuestionSet> securityQuestionSets, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 删除该账号ID对应的密保问题
        this.batchDeleteByMultiParam("basicAuthId", basicAuthId, permId, userInfo, true);
        securityQuestionSets.forEach(record -> {
            record.setId(UuidUtil.get32UUID());
            record.setCreateId(userInfo.getId());
            record.setCreateBy(userInfo.getUserName());
            record.setCreateWay("0");
            record.setCreateTime(new Date());
        });
        Integer count = this.batchInsert(securityQuestionSets);

        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SECURITY_QUESTION_SET, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, securityQuestionSets);
        Assert.isTrue(count==securityQuestionSets.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 根据多参数批量删除密保问题
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-07-16 10:54:20
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "basicAuthId":
                map.put("basicAuthIdEq",paramVal);
                break;
            case "securityQuestionIds":
                List<String> securityQuestionIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(securityQuestionIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",securityQuestionIds);
                map.put("securityQuestionIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SecurityQuestionSet> securityQuestionSets = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(securityQuestionSets)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(securityQuestionSets),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SECURITY_QUESTION_SET, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, securityQuestionSets, null);
        Assert.isTrue(count==securityQuestionSets.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

}
