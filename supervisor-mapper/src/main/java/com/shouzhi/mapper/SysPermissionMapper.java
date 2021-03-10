package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统权限表持久层接口
 * @author WX
 * @date 2020-06-10 09:40:13
 */
@Repository("sysPermissionMapper")
public interface SysPermissionMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysPermission record) throws Exception;

    Integer insertSelective(SysPermission record) throws Exception;

    SysPermission selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysPermission record) throws Exception;

    Integer updateByPrimaryKey(SysPermission record) throws Exception;

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    Integer selectCount();

    /**
     * 根据参数查询系统权限列表
     * @param map
     * @author WX
     * @date 2020-06-10 09:40:13
     */
    List<SysPermission> queryListByPage(Map<String, Object> map);

    /**
     * 批量更新根据ID
     * @param map 参数+SysPermissionIds列表
     * @author WX
     * @date 2020-11-03 10:13:26
     */
    Integer BatchUpdate(Map<String, Object> map) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 10:21:19
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 10:26:06
     */
    List<SysPermission> BatchSelect(Map<String, Object> map);
}