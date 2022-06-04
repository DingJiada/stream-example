package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SysRoleMapper;
import com.shouzhi.pojo.db.*;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统角色表业务层接口实现类
 * @author WX
 * @date 2020-06-12 10:08:30
 */
@Service("sysRoleService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysRoleServiceImpl implements ISysRoleService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysRole record) throws Exception {
        return sysRoleMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysRole record) throws Exception {
        return sysRoleMapper.insertSelective(record);
    }

    @Override
    public SysRole selectByPrimaryKey(String id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysRole record) throws Exception {
        return sysRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysRole record) throws Exception {
        return sysRoleMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询系统角色列表
     * @param record
     * @author WX
     * @date 2020-06-12 09:52:40
     */
    @Override
    public List<SysRole> queryListByPage(SysRole record) {
        return sysRoleMapper.queryListByPage(record);
    }

    /**
     * 新增系统角色
     * @param record
     * @param permId
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    @Override
    public Integer save(SysRole record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入系统角色失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_ROLE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新系统角色
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    @Override
    public Integer update(SysRole record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysRole sr = this.selectByPrimaryKey(record.getId());
        Assert.notNull(sr,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新系统角色失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_ROLE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(sr), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除系统角色
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-12 10:02:05
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除系统角色
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysRole record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_ROLE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该角色关联的成员关系
        sysUserRoleService.batchDeleteByMultiParam("sysRoleId", rowId, permId, userInfo, true);
        // 3.移除与该角色关联的权限关系
        sysRolePermissionService.batchDeleteByMultiParam("sysRoleId", rowId, permId, userInfo, true);
        return count;
    }

    /**
     * 根据角色Id查询系统用户列表
     * @param sysRoleId
     * @author WX
     * @date 2020-08-24 10:59:43
     */
    @Override
    public List<SysUser> findSysUsersByRoleId(String sysRoleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("sysRoleId", sysRoleId);
        return sysUserService.selectByRoleParam(map);
    }

    /**
     * 移除角色成员
     * @param sysRoleId 角色ID
     * @param sysUserId 角色成员ID
     * @param permId    权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-24 14:09:35
     */
    @Override
    public Integer delRoleMember(String sysRoleId, String sysUserId, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Map<String, String> map = new HashMap<>();
        map.put("sysRoleId", sysRoleId);
        map.put("sysUserId", sysUserId);
        return sysUserRoleService.batchDeleteByMultiParam("sysUserIdAndRoleId", map, permId, userInfo, true);
    }

    /**
     * 批量移除角色成员
     * @param sysRoleId  角色ID
     * @param sysUserIds 角色成员ID，对应角色成员列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-24 15:04:36
     */
    @Override
    public Integer batchDelRoleMember(String sysRoleId, String sysUserIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysRoleId", sysRoleId);
        map.put("sysUserIds", Arrays.asList(sysUserIds.split(",")));
        return sysUserRoleService.batchDeleteByMultiParam("sysRoleIdAndUserIds", map, permId, userInfo, true);
    }


}
