package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.db.SysRolePermission;
import com.shouzhi.pojo.po.SysPermissionPo;
import com.shouzhi.pojo.vo.SysPermissionSettingVo;
import com.shouzhi.pojo.vo.TreeNodeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统角色-资源权限表业务层接口
 * @author WX
 * @date 2020-06-12 13:52:16
 */
public interface ISysRolePermissionService {
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


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-26 13:46:10
     */
    Integer batchInsert(List<SysRolePermission> list);


    /**
     * 根据RoleId查询资源权限设置列表
     * @param roleId
     * @author WX
     * @date 2020-06-12 13:45:06
     */
    // List<SysPermissionSettingVo> selectPermissionSettingList(String roleId);

    /**
     * 根据RoleId查询资源权限设置列表
     * @param roleId
     * @author WX
     * @date 2021-01-14 16:18:23
     */
    List<TreeNodeVo> selectPermissionSettingList(String roleId);

    /**
     * 批量保存系统角色-资源权限
     * @param sysRoleId 系统角色ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param permissionIds 资源权限ID列表集合
     * @author WX
     * @date 2020-06-12 15:06:29
     */
    Integer batchSave(String sysRoleId, String permId, List<String> permissionIds, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-23 10:44:16
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;
}
