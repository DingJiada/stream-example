package com.shouzhi.mapper;

import com.shouzhi.pojo.db.Examinee;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考生表持久层接口
 * @author WX
 * @date 2020-08-04 16:57:06
 */
@Repository("examineeMapper")
public interface ExamineeMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(Examinee record) throws Exception;

    Integer insertSelective(Examinee record) throws Exception;

    Examinee selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(Examinee record) throws Exception;

    Integer updateByPrimaryKey(Examinee record) throws Exception;

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-04 16:58:17
     */
    List<Examinee> queryListByPage(Examinee record);

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamineeIds列表
     * @author WX
     * @date 2020-08-05 10:26:57
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 查询考生列表，带头像，前端平台-云监考
     * @param map
     * @author WX
     * @date 2020-08-16 16:03:25
     */
    List<Examinee> foregroundExamineeList(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:01:16
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:08:21
     */
    List<Examinee> BatchSelect(Map<String, Object> map);
}