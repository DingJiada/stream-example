package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.db.SysRolePermission;
import com.shouzhi.pojo.po.SysPermissionPo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统角色-资源权限表持久层接口
 * @author WX
 * @date 2020-06-12 11:27:36
 */
@Repository("sysRolePermissionMapper")
public interface SysRolePermissionMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysRolePermission record) throws Exception;

    Integer insertSelective(SysRolePermission record) throws Exception;

    SysRolePermission selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysRolePermission record) throws Exception;

    Integer updateByPrimaryKey(SysRolePermission record) throws Exception;

    /**
     * 根据参数查询系统角色-资源权限列表
     * @param record
     * @author WX
     * @date 2020-06-12 11:27:36
     */
    List<SysRolePermission> queryListByPage(SysRolePermission record);

    /**
     * 根据RoleId查询所有资源权限信息
     * @param roleId
     * @author WX
     * @date 2020-06-12 13:45:06
     */
    List<SysPermission> selectPermissionsByRoleId(String roleId);

    /**
     * 查询多级菜单全部信息
     * @author WX
     * @date 2020-08-25 15:06:13
     */
    List<SysPermissionPo> multiLevelPermissions();

    /**
     * 根据SysRoleId删除关联的权限信息
     * @param sysRoleId
     * @author WX
     * @date 2020-08-24 10:36:21
     */
    Integer deleteBySysRoleId(String sysRoleId) throws Exception;

    /**
     * 根据SysPermissionId删除关联的权限信息
     * @param sysPermissionId
     * @author WX
     * @date 2020-11-03 09:31:16
     */
    Integer deleteBySysPermissionId(String sysPermissionId) throws Exception;

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-26 13:46:10
     */
    Integer batchInsert(List<SysRolePermission> list);

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 10:40:15
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 10:46:21
     */
    List<SysRolePermission> BatchSelect(Map<String, Object> map);
}