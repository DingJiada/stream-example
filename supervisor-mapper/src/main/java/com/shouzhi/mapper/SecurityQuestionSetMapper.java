package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SecurityQuestionSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 密保问题设置表持久层接口
 * @author WX
 * @date 2020-07-15 17:41:20
 */
@Repository("securityQuestionSetMapper")
public interface SecurityQuestionSetMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SecurityQuestionSet record) throws Exception;

    Integer insertSelective(SecurityQuestionSet record) throws Exception;

    SecurityQuestionSet selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SecurityQuestionSet record) throws Exception;

    Integer updateByPrimaryKey(SecurityQuestionSet record) throws Exception;

    /**
     * 根据参数查询密保问题设置列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:42:15
     */
    List<SecurityQuestionSet> queryListByPage(SecurityQuestionSet record);

    /**
     * 根据账号ID删除该ID对应的所有密保问题
     * @param id
     * @author WX
     * @date 2020-07-16 10:54:20
     */
    Integer deleteByBasicAuthId(String id) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 09:20:15
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 09:26:37
     */
    List<SecurityQuestionSet> BatchSelect(Map<String, Object> map);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-11-23 09:43:19
     */
    Integer batchInsert(List<SecurityQuestionSet> list);
}