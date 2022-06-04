package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统角色表业务层接口
 * @author WX
 * @date 2020-06-12 10:02:05
 */
public interface ISysRoleService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysRole record) throws Exception;

    Integer insertSelective(SysRole record) throws Exception;

    SysRole selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysRole record) throws Exception;

    Integer updateByPrimaryKey(SysRole record) throws Exception;

    /**
     * 根据参数查询系统角色列表
     * @param record
     * @author WX
     * @date 2020-06-12 09:52:40
     */
    List<SysRole> queryListByPage(SysRole record);

    /**
     * 新增系统角色
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    Integer save(SysRole record, String permId, HttpServletRequest req) throws Exception;

    /**
     * 根据ID更新系统角色
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    Integer update(SysRole record, String permId, HttpServletRequest req, String... operType) throws Exception;


    /**
     * 根据ID删除系统角色
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据角色Id查询系统用户列表
     * @param sysRoleId
     * @author WX
     * @date 2020-08-24 10:59:43
     */
    List<SysUser> findSysUsersByRoleId(String sysRoleId);


    /**
     * 移除角色成员
     * @param sysRoleId 角色ID
     * @param sysUserId 角色成员ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-24 14:09:35
     */
    Integer delRoleMember(String sysRoleId, String sysUserId, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量移除角色成员
     * @param sysRoleId 角色ID
     * @param sysUserIds 角色成员ID，对应角色成员列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-24 15:04:36
     */
    Integer batchDelRoleMember(String sysRoleId, String sysUserIds, String permId, HttpServletRequest req) throws Exception;

}
