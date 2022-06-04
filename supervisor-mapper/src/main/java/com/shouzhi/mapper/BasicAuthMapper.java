package com.shouzhi.mapper;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基础认证表持久层接口
 * @author WX
 * @date 2020-07-13 09:36:05
 */
@Repository("basicAuthMapper")
public interface BasicAuthMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(BasicAuth record) throws Exception;

    Integer insertSelective(BasicAuth record) throws Exception;

    BasicAuth selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(BasicAuth record) throws Exception;

    Integer updateByPrimaryKey(BasicAuth record) throws Exception;

    /**
     * 根据参数查询基础认证列表
     * @param record
     * @author WX
     * @date 2020-07-13 09:36:05
     */
    List<BasicAuth> queryListByPage(Map<String, Object> record);

    /**
     * 根据参数查询账号信息
     * @param record
     * @author WX
     * @date 2020-07-13 10:53:00
     */
    BasicAuth selectOneByParam(BasicAuth record);

    /**
     * 更新字段为NULL
     * @param record
     * @author WX
     * @date 2020-07-20 19:04:13
     */
    Integer updateWithNull(BasicAuth record) throws Exception;


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-07-30 17:13:20
     */
    Integer batchInsert(List<BasicAuth> list);


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:49:28
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;


    /**
     * 批量查询根据SysUserIDs
     * @param map
     * @author WX
     * @date 2020-07-31 09:12:06
     */
    List<BasicAuth> BatchSelect(Map<String, Object> map);

    /**
     * 批量更新
     * @param map
     * @author WX
     * @date 2020-08-02 15:39:18
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

}