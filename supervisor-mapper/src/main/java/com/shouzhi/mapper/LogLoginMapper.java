package com.shouzhi.mapper;

import com.shouzhi.pojo.db.LogLogin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 日志登录表持久层接口
 * @author WX
 * @date 2020-06-08 10:32:00
 */
@Repository("logLoginMapper")
public interface LogLoginMapper {
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

}