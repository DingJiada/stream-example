package com.shouzhi.mapper;

import com.shouzhi.pojo.db.ExamVideo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考试(考生)视频表持久层接口
 * @author WX
 * @date 2020-08-06 17:30:16
 */
@Repository("examVideoMapper")
public interface ExamVideoMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExamVideo record) throws Exception;

    Integer insertSelective(ExamVideo record) throws Exception;

    ExamVideo selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExamVideo record) throws Exception;

    Integer updateByPrimaryKey(ExamVideo record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-06 17:30:16
     */
    List<ExamVideo> queryListByPage(ExamVideo record);

    /**
     * 根据参数查询信息
     * @param record
     * @author WX
     * @date 2020-08-11 09:27:00
     */
    ExamVideo selectOneByParam(ExamVideo record);

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamVideoIds列表
     * @author WX
     * @date 2020-08-06 18:27:35
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 15:56:20
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 16:00:32
     */
    List<ExamVideo> BatchSelect(Map<String, Object> map);
}