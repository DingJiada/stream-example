package com.shouzhi.mapper;

import com.shouzhi.pojo.db.ShortcutMenu;
import com.shouzhi.pojo.db.SysPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 快捷菜单表持久层接口
 * @author WX
 * @date 2021-01-21 17:03:36
 */
@Repository("shortcutMenuMapper")
public interface ShortcutMenuMapper {
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
}