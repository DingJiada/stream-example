package com.shouzhi.service.impl.db;

import com.shouzhi.mapper.LogOperDetailMapper;
import com.shouzhi.mapper.LogOperMapper;
import com.shouzhi.pojo.db.LogOper;
import com.shouzhi.pojo.db.LogOperDetail;
import com.shouzhi.service.interf.db.ILogOperDetailService;
import com.shouzhi.service.interf.db.ILogOperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志操作详情表业务层接口实现类
 * @author WX
 * @date 2020-06-09 15:58:31
 */
@Service("logOperDetailService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class LogOperDetailServiceImpl implements ILogOperDetailService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogOperDetailMapper logOperDetailMapper;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return logOperDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(LogOperDetail record) throws Exception {
        return logOperDetailMapper.insert(record);
    }

    @Override
    public Integer insertSelective(LogOperDetail record) throws Exception {
        return logOperDetailMapper.insertSelective(record);
    }

    @Override
    public LogOperDetail selectByPrimaryKey(String id) {
        return logOperDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(LogOperDetail record) throws Exception {
        return logOperDetailMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(LogOperDetail record) throws Exception {
        return logOperDetailMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询日志操作详情列表
     * @param record
     * @author WX
     * @date 2020-06-09 15:23:45
     */
    @Override
    public List<LogOperDetail> queryListByPage(LogOperDetail record) {
        return logOperDetailMapper.queryListByPage(record);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-11-20 17:11:26
     */
    @Override
    public Integer batchInsert(List<LogOperDetail> list) {
        return logOperDetailMapper.batchInsert(list);
    }
}
