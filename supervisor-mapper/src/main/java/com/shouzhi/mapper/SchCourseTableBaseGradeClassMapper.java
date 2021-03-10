package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SchCourseTableBaseGradeClass;
import org.springframework.stereotype.Repository;

/**
 * 学校基础课程表年级班级表持久层接口
 * @author WX
 * @date 2020-12-04 09:32:16
 */
@Repository("schCourseTableBaseGradeClassMapper")
public interface SchCourseTableBaseGradeClassMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableBaseGradeClass record) throws Exception;

    Integer insertSelective(SchCourseTableBaseGradeClass record) throws Exception;

    SchCourseTableBaseGradeClass selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableBaseGradeClass record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableBaseGradeClass record) throws Exception;
}