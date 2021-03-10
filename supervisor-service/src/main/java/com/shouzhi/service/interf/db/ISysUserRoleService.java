package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysUserRole;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统用户-角色表业务层接口
 * @author WX
 * @date 2020-06-15 15:37:58
 */
public interface ISysUserRoleService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(SysUserRole record) throws Exception;

    Integer insertSelective(SysUserRole record) throws Exception;

    SysUserRole selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SysUserRole record) throws Exception;

    Integer updateByPrimaryKey(SysUserRole record) throws Exception;


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


    /**
     * 新增系统用户角色关联ID
     * @param record
     * @param permId
     * @author WX
     * @date 2020-07-29 16:50:24
     */
    Integer save(SysUserRole record, String permId, HttpServletRequest req) throws Exception;

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
     */
    Integer batchInsert(List<SysUserRole> list);


    /**
     * 批量插入根据单个SysUserId，含插入操作日志
     * @param sysUserId
     * @param sysRoleIds
     * @param permId
     * @author WX
     * @date 2020-07-29 18:02:10
     */
    Integer batchSaveBySysUserId(String sysUserId, String sysRoleIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 批量插入根据多个SysUserId，含插入操作日志
     * @param sysUserIds
     * @param sysRoleIds
     * @param permId
     * @author WX
     * @date 2020-07-30 10:22:46
     */
    Integer batchSaveBySysUserIds(String sysUserIds, String sysRoleIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 新增用户角色信息(wr_sys_user_role)
     * @param sysUserId 系统用户ID
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-29 19:41:23
     */
    Integer updateSysUserRole(String sysUserId, String sysRoleIds, String permId, HttpServletRequest req) throws Exception;

    /**
     * 批量新增用户角色信息(wr_sys_user_role)
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 09:42:43
     */
    Integer batchUpdateSysUserRole(String sysUserIds, String sysRoleIds, String permId, HttpServletRequest req) throws Exception;


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-23 10:57:23
     */
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;
}
