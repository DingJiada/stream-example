package com.shouzhi.mapper;

import com.shouzhi.pojo.db.ExaminationHallInvigilators;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考场考试-监考老师关联表持久层接口
 * @author WX
 * @date 2020-08-03 13:53:40
 */
@Repository("examinationHallInvigilatorsMapper")
public interface ExaminationHallInvigilatorsMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ExaminationHallInvigilators record) throws Exception;

    Integer insertSelective(ExaminationHallInvigilators record) throws Exception;

    ExaminationHallInvigilators selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ExaminationHallInvigilators record) throws Exception;

    Integer updateByPrimaryKey(ExaminationHallInvigilators record) throws Exception;

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-03 13:57:19
     */
    Integer batchInsert(List<ExaminationHallInvigilators> list);

    /**
     * 根据ExaminationHallId删除关联的用户ID
     * @param examinationHallId
     * @author WX
     * @date 2020-08-03 13:57:19
     */
    Integer deleteByExaminationHallId(String examinationHallId) throws Exception;

    /**
     * 根据ExaminationHallId批量删除关联的用户ID
     * @param list ExaminationHallIds列表
     * @author WX
     * @date 2020-08-03 13:57:19
     */
    Integer batchDeleteByExaminationHallId(List<String> list) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:13:18
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 17:18:27
     */
    List<ExaminationHallInvigilators> BatchSelect(Map<String, Object> map);

}