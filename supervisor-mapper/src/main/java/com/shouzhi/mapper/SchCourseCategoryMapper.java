package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchCourseCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校课程类别表持久层接口
 * @author WX
 * @date 2020-12-02 14:02:06
 */
@Repository("schCourseCategoryMapper")
public interface SchCourseCategoryMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseCategory record) throws Exception;

    Integer insertSelective(SchCourseCategory record) throws Exception;

    SchCourseCategory selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseCategory record) throws Exception;

    Integer updateByPrimaryKey(SchCourseCategory record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-02 14:08:19
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-02 14:15:39
     */
    List<SchCourseCategory> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-02 14:19:57
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-02 14:24:38
     */
    List<SchCourseCategory> BatchSelect(Map<String, Object> map);
}