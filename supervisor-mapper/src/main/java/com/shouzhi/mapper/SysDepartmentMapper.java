package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysDepartment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统部门表持久层接口
 * @author WX
 * @date 2020-11-30 11:41:23
 */
@Repository("sysDepartmentMapper")
public interface SysDepartmentMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysDepartment record) throws Exception;

    Integer insertSelective(SysDepartment record) throws Exception;

    SysDepartment selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysDepartment record) throws Exception;

    Integer updateByPrimaryKey(SysDepartment record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-30 11:43:42
     */
    Integer selectCount();

    /**
     * 根据参数查询系统权限列表
     * @param map
     * @author WX
     * @date 2020-11-30 11:46:37
     */
    List<SysDepartment> queryListByPage(Map<String, Object> map);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-30 14:03:09
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-30 14:06:56
     */
    List<SysDepartment> BatchSelect(Map<String, Object> map);
}