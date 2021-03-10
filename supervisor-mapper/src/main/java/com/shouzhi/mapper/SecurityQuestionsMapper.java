package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SecurityQuestions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 密保问题表持久层接口
 * @author WX
 * @date 2020-07-15 17:15:03
 */
@Repository("securityQuestionsMapper")
public interface SecurityQuestionsMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SecurityQuestions record) throws Exception;

    Integer insertSelective(SecurityQuestions record) throws Exception;

    SecurityQuestions selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SecurityQuestions record) throws Exception;

    Integer updateByPrimaryKey(SecurityQuestions record) throws Exception;

    /**
     * 根据参数查询密保问题列表
     * @param record
     * @author WX
     * @date 2020-07-15 17:15:03
     */
    List<SecurityQuestions> queryListByPage(SecurityQuestions record);

    /**
     * 根据认证账号ID查询密保列表
     * @param basicAuthId
     * @author WX
     * @date 2020-07-21 11:14:21
     */
    List<SecurityQuestions> selectByBasicAuthId(String basicAuthId);

}