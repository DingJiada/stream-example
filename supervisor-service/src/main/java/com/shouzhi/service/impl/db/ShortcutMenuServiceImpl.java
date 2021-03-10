package com.shouzhi.service.impl.db;

import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.ShortcutMenuMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ShortcutMenu;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.IShortcutMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 快捷菜单表业务层接口实现类
 * @author WX
 * @date 2021-01-21 17:28:59
 */
@Service("shortcutMenuService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ShortcutMenuServiceImpl implements IShortcutMenuService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShortcutMenuMapper shortcutMenuMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return shortcutMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(ShortcutMenu record) throws Exception {
        return shortcutMenuMapper.insert(record);
    }

    @Override
    public Integer insertSelective(ShortcutMenu record) throws Exception {
        return shortcutMenuMapper.insertSelective(record);
    }

    @Override
    public ShortcutMenu selectByPrimaryKey(String id) {
        return shortcutMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ShortcutMenu record) throws Exception {
        return shortcutMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(ShortcutMenu record) throws Exception {
        return shortcutMenuMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询快捷菜单列表
     * @param map
     * @author WX
     * @date 2021-01-21 17:04:53
     */
    @Override
    public List<ShortcutMenu> queryListByPage(Map<String, Object> map) {
        return shortcutMenuMapper.queryListByPage(map);
    }

    /**
     * 根据sysUserId查询所有资源权限信息
     * @param sysUserId
     * @author WX
     * @date 2021-01-21 17:08:08
     */
    @Override
    public List<SysPermission> selectPermissionsByUserId(String sysUserId) {
        return shortcutMenuMapper.selectPermissionsByUserId(sysUserId);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-01-21 17:13:19
     */
    @Override
    public Integer batchInsert(List<ShortcutMenu> list) throws Exception {
        return shortcutMenuMapper.batchInsert(list);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2021-01-21 17:16:23
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return shortcutMenuMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2021-01-21 17:19:39
     */
    @Override
    public List<ShortcutMenu> BatchSelect(Map<String, Object> map) {
        return shortcutMenuMapper.BatchSelect(map);
    }


    /**
     * 批量保存系统用户-资源权限
     * @param sysUserId     系统用户ID
     * @param permId        权限ID或菜单ID(仅限于最后级别的菜单)
     * @param permissionIds 资源权限ID列表集合
     * @param req
     * @author WX
     * @date 2021-01-21 17:32:20
     */
    @Override
    public Integer batchSave(String sysUserId, String permId, List<String> permissionIds, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 根据sysUserId删除
        this.batchDeleteByMultiParam("sysUserId", sysUserId, permId, userInfo, false);
        // 仅仅是取消全部快捷菜单，直接返回
        if(CollectionUtils.isEmpty(permissionIds)) return 0;
        List<ShortcutMenu> shortcutMenus = permissionIds.stream().distinct().map(s -> new ShortcutMenu(UuidUtil.get32UUID(), s, sysUserId, userInfo.getId(), userInfo.getUserName(), null, null)).collect(Collectors.toList());
        //  批量插入
        Integer count = this.batchInsert(shortcutMenus);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SHORTCUT_MENU, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, shortcutMenus);
        Assert.isTrue(count==permissionIds.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 根据多参数批量删除
     * @param paramKey   删除参数key
     * @param paramVal   删除参数val
     * @param permId
     * @param userInfo
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2021-01-21 17:38:16
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "sysPermissionId":
                map.put("sysPermissionIdEq",paramVal);
                break;
            case "sysPermissionIds":
                List<String> sysPermissionIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(sysPermissionIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",sysPermissionIds);
                map.put("sysPermissionIdIn","1");
                break;
            case "sysUserId":
                map.put("sysUserIdEq",paramVal);
                break;
            case "sysUserIds":
                List<String> sysUserIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(sysUserIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",sysUserIds);
                map.put("sysUserIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<ShortcutMenu> shortcutMenus = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(shortcutMenus)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(shortcutMenus),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SHORTCUT_MENU, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, shortcutMenus, null);
        Assert.isTrue(count==shortcutMenus.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }
}
