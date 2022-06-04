package com.shouzhi.mapper;

import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统用户-角色表持久层接口
 * @author WX
 * @date 2020-06-15 15:21:05
 */
@Repository("sysUserRoleMapper")
public interface SysUserRoleMapper {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysUserRole record) throws Exception;

    Integer insertSelective(SysUserRole record) throws Exception;

    SysUserRole selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysUserRole record) throws Exception;

    Integer updateByPrimaryKey(SysUserRole record) throws Exception;

    /**
     * 根据userId查询所有角色+权限信息
     * @param id
     * @author WX
     * @date 2020-06-15 15:21:05
     */
    List<SysRole> selectRolesPermissionsByUserId(String id);

    /**
     * 根据userId查询所有角色信息
     * @param id
     * @author WX
     * @date 2020-07-21 19:35:10
     */
    List<SysRole> selectRolesByUserId(String id);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-07-29 18:02:10
     */
    Integer batchInsert(List<SysUserRole> list);

    /**
     * 根据SysUserId删除关联的角色ID
     * @param sysUserId
     * @author WX
     * @date 2020-07-30 09:07:42
     */
    Integer deleteBySysUserId(String sysUserId) throws Exception;

    /**
     * 根据SysRoleId删除关联的角色ID
     * @param sysRoleId
     * @author WX
     * @date 2020-08-24 10:24:18
     */
    Integer deleteBySysRoleId(String sysRoleId) throws Exception;

    /**
     * 根据SysUserId和SysRoleId删除关联的角色
     * @param sysUserRole
     * @author WX
     * @date 2020-08-24 14:57:21
     */
    Integer deleteBySysUserAndRoleId(SysUserRole sysUserRole) throws Exception;

    /**
     * 根据SysUserId批量删除关联的角色ID
     * @param list SysUserIds列表
     * @author WX
     * @date 2020-07-30 09:57:23
     */
    Integer batchDeleteBySysUserId(List<String> list) throws Exception;

    /**
     * 根据SysUserId和SysRoleId批量删除关联的角色
     * @param map
     * @author WX
     * @date 2020-08-24 15:33:52
     */
    Integer batchDeleteBySysUserIdAndRoleId(Map<String, Object> map) throws Exception;


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 11:04:19
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 11:11:53
     */
    List<SysUserRole> BatchSelect(Map<String, Object> map);
}