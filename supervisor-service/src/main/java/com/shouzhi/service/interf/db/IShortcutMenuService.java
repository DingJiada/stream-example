package com.shouzhi.service.interf.db;

import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ShortcutMenu;
import com.shouzhi.pojo.db.SysPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 快捷菜单表业务层接口
 * @author WX
 * @date 2021-01-21 17:24:47
 */
public interface IShortcutMenuService {
    Integer deleteByPrimaryKey(String id) throws Exception;

    Integer insert(ShortcutMenu record) throws Exception;

    Integer insertSelective(ShortcutMenu record) throws Exception;

    ShortcutMenu selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(ShortcutMenu record) throws Exception;

    Integer updateByPrimaryKey(ShortcutMenu record) throws Exception;

    /**
     * 根据参数查询快捷菜单列表
     * @param map
     * @author WX
     * @date 2021-01-21 17:04:53
     */
    List<ShortcutMenu> queryListByPage(Map<String, Object> map);

    /**
     * 根据sysUserId查询所有资源权限信息
     * @param sysUserId
     * @author WX
     * @date 2021-01-21 17:08:08
     */
    List<SysPermission> selectPermissionsByUserId(String sysUserId);

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-01-21 17:13:19
     */
    Integer batchInsert(List<ShortcutMenu> list) throws Exception;

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2021-01-21 17:16:23
     */
    Integer batchDelete(Map<String, Object> map) throws Exception;

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2021-01-21 17:19:39
     */
    List<ShortcutMenu> BatchSelect(Map<String, Object> map);


    /**
     * 批量保存系统用户-资源权限
     * @param sysUserId 系统用户ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param permissionIds 资源权限ID列表集合
     * @author WX
     * @date 2021-01-21 17:32:20
     */
    Integer batchSave(String sysUserId, String permId, List<String> permissionIds, HttpServletRequest req) throws Exception;

    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2021-01-21 17:38:16
     */
    Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception;
}
