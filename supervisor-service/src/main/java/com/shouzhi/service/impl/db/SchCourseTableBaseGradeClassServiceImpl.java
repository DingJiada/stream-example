package com.shouzhi.service.impl.db;

import com.shouzhi.mapper.SchCourseTableBaseGradeClassMapper;
import com.shouzhi.pojo.db.SchCourseTableBaseGradeClass;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISchCourseTableBaseGradeClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学校基础课程表年级班级表业务层接口实现类
 * @author WX
 * @date 2020-12-04 09:36:03
 */
@Service("schCourseTableBaseGradeClassService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchCourseTableBaseGradeClassServiceImpl implements ISchCourseTableBaseGradeClassService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchCourseTableBaseGradeClassMapper schCourseTableBaseGradeClassMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schCourseTableBaseGradeClassMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchCourseTableBaseGradeClass record) throws Exception {
        return schCourseTableBaseGradeClassMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchCourseTableBaseGradeClass record) throws Exception {
        return schCourseTableBaseGradeClassMapper.insertSelective(record);
    }

    @Override
    public SchCourseTableBaseGradeClass selectByPrimaryKey(String id) {
        return schCourseTableBaseGradeClassMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchCourseTableBaseGradeClass record) throws Exception {
        return schCourseTableBaseGradeClassMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchCourseTableBaseGradeClass record) throws Exception {
        return schCourseTableBaseGradeClassMapper.updateByPrimaryKey(record);
    }
}
