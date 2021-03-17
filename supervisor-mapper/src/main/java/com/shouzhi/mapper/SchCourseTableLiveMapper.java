package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchCourseTableLive;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校直播课程表表持久层接口
 * @author WX
 * @date 2021-02-23 10:48:09
 */
@Repository("schCourseTableLiveMapper")
public interface SchCourseTableLiveMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableLive record) throws Exception;

    Integer insertSelective(SchCourseTableLive record) throws Exception;

    SchCourseTableLive selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableLive record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableLive record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-02-23 10:49:18
     */
    SchCourseTableLive selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2021-02-23 10:51:26
     */
    Integer selectCount();

    /**
     * 根据参数查询列表，仅用于前台在线巡课调用，其余地方不可调用！
     * @param map
     * @author WX
     * @date 2021-02-23 10:48:09
     */
    // List<SchCourseTableLive> foregroundListByPage(Map<String, Object> map);

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2021-02-23 10:54:29
     */
    List<SchCourseTableLive> queryListByPage(Map<String, Object> map);


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-12 17:43:36
     */
    Integer batchInsert(List<SchCourseTableLive> list) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2021-02-23 10:58:37
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2021-02-23 11:06:18
     */
    List<SchCourseTableLive> BatchSelect(Map<String, Object> map);

    /**
     * 批量更新
     * @param map
     * @author Dingjd
     * @date 2021/3/17 15:15
     **/
    Integer batchUpdate(Map<String, Object> map);
}