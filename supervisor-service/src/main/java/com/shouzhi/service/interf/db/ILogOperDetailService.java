package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.LogOperDetail;

import java.util.List;

/**
 * 日志操作详情表业务层接口
 * @author WX
 * @date 2020-06-09 15:58:31
 */
public interface ILogOperDetailService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(LogOperDetail record) throws Exception;

    Integer insertSelective(LogOperDetail record) throws Exception;

    LogOperDetail selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(LogOperDetail record) throws Exception;

    Integer updateByPrimaryKey(LogOperDetail record) throws Exception;

    /**
     * 根据参数查询日志操作详情列表
     * @param record
     * @author WX
     * @date 2020-06-09 15:23:45
     */
    List<LogOperDetail> queryListByPage(LogOperDetail record);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-11-20 17:11:26
     */
    Integer batchInsert(List<LogOperDetail> list);
}
