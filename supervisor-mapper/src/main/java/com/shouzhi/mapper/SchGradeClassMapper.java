package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchGradeClass;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校年级班级表持久层接口
 * @author WX
 * @date 2020-12-01 15:48:17
 */
@Repository("schGradeClassMapper")
public interface SchGradeClassMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchGradeClass record) throws Exception;

    Integer insertSelective(SchGradeClass record) throws Exception;

    SchGradeClass selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchGradeClass record) throws Exception;

    Integer updateByPrimaryKey(SchGradeClass record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-01 15:51:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-01 15:59:18
     */
    List<SchGradeClass> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-01 16:06:31
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-01 16:13:30
     */
    List<SchGradeClass> BatchSelect(Map<String, Object> map);

    /**
     * 批量新增
     * @author Dingjd
     * @date 2021/3/15 13:24
     * @param list
     * @return java.lang.Integer
     **/
    Integer batchInsert(List<SchGradeClass> list);
}