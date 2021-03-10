package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchSpace;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校空间表持久层接口
 * @author WX
 * @date 2020-11-05 17:18:21
 */
@Repository("schSpaceMapper")
public interface SchSpaceMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchSpace record) throws Exception;

    Integer insertSelective(SchSpace record) throws Exception;

    SchSpace selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchSpace record) throws Exception;

    Integer updateByPrimaryKey(SchSpace record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-01-20 17:51:36
     */
    SchSpace selectOneByParam(Map<String, Object> map);

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-11-05 17:18:21
     */
    List<SchSpace> queryListByPage(SchSpace record);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-02 15:32:06
     */
    Integer batchInsert(List<SchSpace> list) throws Exception;

    /**
     * 批量更新根据ID
     * @param map 参数+schSpaceIds列表
     * @author WX
     * @date 2020-11-06 11:20:09
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-21 18:14:23
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-21 18:18:07
     */
    List<SchSpace> BatchSelect(Map<String, Object> map);
}