package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.RegexUtil;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.LogLoginMapper;
import com.shouzhi.mapper.LogOperMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.LogLogin;
import com.shouzhi.pojo.db.LogOper;
import com.shouzhi.pojo.db.LogOperDetail;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.constants.DBEnum;
import com.shouzhi.service.interf.db.ILogLoginService;
import com.shouzhi.service.interf.db.ILogOperDetailService;
import com.shouzhi.service.interf.db.ILogOperService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 日志操作表业务层接口实现类
 * @author WX
 * @date 2020-06-09 14:48:40
 */
@Service("logOperService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class LogOperServiceImpl implements ILogOperService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogOperMapper logOperMapper;

    @Autowired
    private ILogOperDetailService logOperDetailService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return logOperMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(LogOper record) throws Exception {
        return logOperMapper.insert(record);
    }

    @Override
    public Integer insertSelective(LogOper record) throws Exception {
        return logOperMapper.insertSelective(record);
    }

    @Override
    public LogOper selectByPrimaryKey(String id) {
        return logOperMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(LogOper record) throws Exception {
        return logOperMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(LogOper record) throws Exception {
        return logOperMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询日志操作列表
     *
     * @param record
     * @author WX
     * @date 2020-06-09 14:35:23
     */
    @Override
    public List<LogOper> queryListByPage(LogOper record) {
        return logOperMapper.queryListByPage(record);
    }

    /**
     * 插入操作日志和操作日志详情
     * 为什么不直接接收两个对象？因为那样每个插入日志的service都会有new两个对象并set字段的代码，臃肿
     * @author WX
     * @date 2020-06-11 09:28:51
     */
    @Override
    public boolean insertLogOperAndDetail(String tabName, String operType, String permId, String isCascade, String cascadeId, BasicAuth userInfo,
                                          String rowIdVal, String oldVal, String newVal) throws Exception {
        LogOper logOper = new LogOper(UuidUtil.get32UUID(), tabName, operType, permId, isCascade, cascadeId, userInfo.getId(), userInfo.getUserName());
        LogOperDetail logOperDetail = new LogOperDetail(UuidUtil.get32UUID(), logOper.getId(), rowIdVal, oldVal, newVal);
        Integer logOperCount = this.insertSelective(logOper);
        Integer logOperDetailCount = logOperDetailService.insertSelective(logOperDetail);
        Assert.isTrue(logOperCount==1&&logOperDetailCount==1, "插入操作日志或操作日志详情失败！");
        return true;
    }

    /**
     * 批量插入操作日志和操作日志详情
     * @author WX
     * @date 2020-11-20 14:47:13
     */
    @Override
    public boolean batchInsertLogOperAndDetail(String tabName, String operType, String permId, String isCascade, String cascadeId, BasicAuth userInfo, String rowIdFiledName, List<?> oldVals, List<?> newVals) throws Exception {
        Integer valuesSize = 0;
        LogOper logOper = new LogOper(UuidUtil.get32UUID(), tabName, operType, permId, isCascade, cascadeId, userInfo.getId(), userInfo.getUserName());
        List<LogOperDetail> logOperDetails = new ArrayList<>();
        switch(operType){
            case DBConst.OPER_TYPE_BATCH_INSERT:
                valuesSize = newVals.size();
                logOperDetails = newVals.stream().map(o -> {
                    String newVal = JSON.toJSONString(o);
                    return new LogOperDetail(UuidUtil.get32UUID(), logOper.getId(), RegexUtil.matchOne("\""+rowIdFiledName+"\":\"(.*?)\"", newVal, 1), null, newVal);
                }).collect(Collectors.toList());
                break;
            case DBConst.OPER_TYPE_BATCH_DELETE:
                valuesSize = oldVals.size();
                logOperDetails = oldVals.stream().map(o -> {
                    String oldVal = JSON.toJSONString(o);
                    return new LogOperDetail(UuidUtil.get32UUID(), logOper.getId(), RegexUtil.matchOne("\"" + rowIdFiledName + "\":\"(.*?)\"", oldVal, 1), oldVal, null);
                }).collect(Collectors.toList());
                break;
            case DBConst.OPER_TYPE_BATCH_UPDATE:
                // TODO 批量更新 (目前未做记录处理)
                valuesSize = oldVals.size();
                break;
        }
        Integer logOperCount = this.insertSelective(logOper);
        Integer logOperDetailCount = logOperDetailService.batchInsert(logOperDetails);
        Assert.isTrue(logOperCount==1&&valuesSize.equals(logOperDetailCount), "批量插入操作日志或操作日志详情失败！");
        return true;
    }

}
