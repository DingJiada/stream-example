package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchCourseTableBase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校基础课程表表持久层接口
 * @author WX
 * @date 2020-12-03 14:05:54
 */
@Repository("schCourseTableBaseMapper")
public interface SchCourseTableBaseMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableBase record) throws Exception;

    Integer insertSelective(SchCourseTableBase record) throws Exception;

    SchCourseTableBase selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableBase record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableBase record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-29 15:38:06
     */
    SchCourseTableBase selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2020-12-03 14:10:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表，仅用于前台在线巡课调用，其余地方不可调用！
     * @param map
     * @author WX
     * @date 2020-12-29 17:20:19
     */
    List<SchCourseTableBase> foregroundListByPage(Map<String, Object> map);

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-03 14:14:23
     */
    List<SchCourseTableBase> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-03 14:19:37
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-03 14:23:16
     */
    List<SchCourseTableBase> BatchSelect(Map<String, Object> map);
}