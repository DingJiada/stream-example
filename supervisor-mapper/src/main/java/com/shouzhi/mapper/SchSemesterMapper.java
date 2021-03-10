package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchSemester;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学校学期表持久层接口
 * @author WX
 * @date 2020-12-04 11:14:26
 */
@Repository("schSemesterMapper")
public interface SchSemesterMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchSemester record) throws Exception;

    Integer insertSelective(SchSemester record) throws Exception;

    SchSemester selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchSemester record) throws Exception;

    Integer updateByPrimaryKey(SchSemester record) throws Exception;

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-07 09:45:06
     */
    SchSemester selectOneByParam(Map<String, Object> map);


    /**
     * 查询总数
     * @author WX
     * @date 2020-12-04 11:19:06
     */
    Integer selectCount();

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-04 11:23:17
     */
    List<SchSemester> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-04 11:28:50
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-04 11:32:09
     */
    List<SchSemester> BatchSelect(Map<String, Object> map);
}