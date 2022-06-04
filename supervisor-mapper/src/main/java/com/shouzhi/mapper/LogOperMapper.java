package com.shouzhi.mapper;

import com.shouzhi.pojo.db.LogOper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志操作表持久层接口
 * @author WX
 * @date 2020-06-09 14:35:23
 */
@Repository("logOperMapper")
public interface LogOperMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(LogOper record) throws Exception;

    Integer insertSelective(LogOper record) throws Exception;

    LogOper selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(LogOper record) throws Exception;

    Integer updateByPrimaryKey(LogOper record) throws Exception;

    /**
     * 根据参数查询日志操作列表
     * @param record
     * @author WX
     * @date 2020-06-09 14:35:23
     */
    List<LogOper> queryListByPage(LogOper record);

}