package com.shouzhi.mapper;

import com.shouzhi.pojo.db.ExaminationHall;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考场考试表持久层接口
 * @author WX
 * @date 2020-08-03 12:31:16
 */
@Repository("examinationHallMapper")
public interface ExaminationHallMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExaminationHall record) throws Exception;

    Integer insertSelective(ExaminationHall record) throws Exception;

    ExaminationHall selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExaminationHall record) throws Exception;

    Integer updateByPrimaryKey(ExaminationHall record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-03 12:49:23
     */
    List<ExaminationHall> queryListByPage(ExaminationHall record);

    /**
     * 根据用户表的参数字段查询考试信息，比如：根据用户ID、根据用户姓名等，不涉及groupBy
     * @param map
     * @author WX
     * @date 2020-08-05 18:43:18
     */
    List<ExaminationHall> selectByUserParam(Map<String, Object> map);


    /**
     * 批量更新 根据ID
     * @param map 参数+ExaminationHallIds列表
     * @author WX
     * @date 2020-08-04 14:08:42
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:46:09
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:51:39
     */
    List<ExaminationHall> BatchSelect(Map<String, Object> map);
}