package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SchCourseTableBaseGradeClass;

/**
 * 学校基础课程表年级班级表业务层接口
 * @author WX
 * @date 2020-12-04 09:34:55
 */
public interface ISchCourseTableBaseGradeClassService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SchCourseTableBaseGradeClass record) throws Exception;

    Integer insertSelective(SchCourseTableBaseGradeClass record) throws Exception;

    SchCourseTableBaseGradeClass selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SchCourseTableBaseGradeClass record) throws Exception;

    Integer updateByPrimaryKey(SchCourseTableBaseGradeClass record) throws Exception;
}
