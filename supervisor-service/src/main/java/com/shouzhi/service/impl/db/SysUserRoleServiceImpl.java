package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SysUserRoleMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysRolePermission;
import com.shouzhi.pojo.db.SysUserRole;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISysUserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户-角色表业务层接口实现类
 * @author WX
 * @date 2020-06-15 15:39:36
 */
@Service("sysUserRoleService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysUserRoleServiceImpl implements ISysUserRoleService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysUserRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysUserRole record) throws Exception {
        return sysUserRoleMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysUserRole record) throws Exception {
        return sysUserRoleMapper.insertSelective(record);
    }

    @Override
    public SysUserRole selectByPrimaryKey(String id) {
        return sysUserRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysUserRole record) throws Exception {
        return sysUserRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysUserRole record) throws Exception {
        return sysUserRoleMapper.updateByPrimaryKey(record);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 11:04:19
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return sysUserRoleMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 11:11:53
     */
    @Override
    public List<SysUserRole> BatchSelect(Map<String, Object> map) {
        return sysUserRoleMapper.BatchSelect(map);
    }

    /**
     * 新增系统用户角色关联ID
     * @param record
     * @param permId
     * @author WX
     * @date 2020-07-29 16:50:24
     */
    @Override
    public Integer save(SysUserRole record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入系统用户角色关联ID失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER_ROLE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据userId查询所有角色+权限信息
     * @param id
     * @author WX
     * @date 2020-06-15 15:21:05
     */
    @Override
    public List<SysRole> selectRolesPermissionsByUserId(String id) {
        return sysUserRoleMapper.selectRolesPermissionsByUserId(id);
    }

    /**
     * 根据userId查询所有角色信息
     * @param id
     * @author WX
     * @date 2020-07-21 19:35:10
     */
    @Override
    public List<SysRole> selectRolesByUserId(String id) {
        return sysUserRoleMapper.selectRolesByUserId(id);
    }

    /**
     * 批量插入
     * @param list
     */
    @Override
    public Integer batchInsert(List<SysUserRole> list) {
        return sysUserRoleMapper.batchInsert(list);
    }

    /**
     * 批量插入，含插入操作日志
     * @param sysUserId
     * @param sysRoleIds
     * @param permId
     * @author WX
     * @date 2020-07-29 18:02:10
     */
    @Override
    public Integer batchSaveBySysUserId(String sysUserId, String sysRoleIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 组建sysUserRoles
        List<SysUserRole> sysUserRoles = this.convertSysUserRoles(sysUserId, sysRoleIds, userInfo);
        // 批量插入角色
        Integer count = this.batchInsert(sysUserRoles);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER_ROLE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, sysUserRoles);
        Assert.isTrue(count==sysUserRoles.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    private List<SysUserRole> convertSysUserRoles(String sysUserId, String sysRoleIds, BasicAuth userInfo){
        return Arrays.stream(sysRoleIds.split(","))
                .map(sysRoleId -> new SysUserRole(UuidUtil.get32UUID(), sysUserId, sysRoleId, userInfo.getId(), userInfo.getUserName()))
                .collect(Collectors.toList());
    }

    /**
     * 批量插入根据多个SysUserId，含插入操作日志
     * @param sysUserIds
     * @param sysRoleIds
     * @param permId
     * @author WX
     * @date 2020-07-30 10:22:46
     */
    @Override
    public Integer batchSaveBySysUserIds(String sysUserIds, String sysRoleIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        Arrays.stream(sysUserIds.split(",")).forEach(sysUserId -> sysUserRoles.addAll(this.convertSysUserRoles(sysUserId, sysRoleIds, userInfo)));
        // 批量插入角色
        Integer count = this.batchInsert(sysUserRoles);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER_ROLE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, sysUserRoles);
        Assert.isTrue(count==sysUserRoles.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }


    /**
     * 新增用户角色信息(wr_sys_user_role)
     * @param sysUserId  系统用户ID
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-29 19:41:23
     */
    @Override
    public Integer updateSysUserRole(String sysUserId, String sysRoleIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 根据用户ID删除角色关联
        this.batchDeleteByMultiParam("sysUserId", sysUserId, permId, userInfo, false);
        // 仅仅是取消全部角色关联，直接返回
        if(StringUtils.isBlank(sysRoleIds)) return 0;
        // 批量插入用户ID角色ID
        this.batchSaveBySysUserId(sysUserId, sysRoleIds, permId, req);
        return 1;
    }

    /**
     * 批量新增用户角色信息(wr_sys_user_role)
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId     权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-07-30 09:42:43
     */
    @Override
    public Integer batchUpdateSysUserRole(String sysUserIds, String sysRoleIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 批量删除
        this.batchDeleteByMultiParam("sysUserIds", Arrays.asList(sysUserIds.split(",")), permId, userInfo, false);
        // 仅仅是取消全部角色关联，直接返回
        if(StringUtils.isBlank(sysRoleIds)) return 0;
        // 批量插入
        this.batchSaveBySysUserIds(sysUserIds, sysRoleIds, permId, req);
        return 1;
    }


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-23 10:57:23
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "sysUserId":
                map.put("sysUserIdEq",paramVal);
                break;
            case "sysRoleId":
                map.put("sysRoleIdEq",paramVal);
                break;
            case "sysUserIds":
                List<String> sysUserIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(sysUserIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",sysUserIds);
                map.put("sysUserIdIn","1");
                break;
            case "sysUserIdAndRoleId":
                Map<String, String> sysUserIdAndRoleId =  (Map<String, String>)paramVal;
                Assert.isTrue(MapUtils.isNotEmpty(sysUserIdAndRoleId)&&sysUserIdAndRoleId.size()==2,"COMMON_INVALID_ARG_ERROR");
                map.put("sysUserId",sysUserIdAndRoleId.get("sysUserId"));
                map.put("sysRoleId",sysUserIdAndRoleId.get("sysRoleId"));
                map.put("sysUserIdAndRoleIdEq","1");
                break;
            case "sysRoleIdAndUserIds":
                Map<String, String> sysRoleIdAndUserIds =  (Map<String, String>)paramVal;
                Assert.isTrue(MapUtils.isNotEmpty(sysRoleIdAndUserIds)&&sysRoleIdAndUserIds.size()==2,"COMMON_INVALID_ARG_ERROR");
                map.put("list",sysRoleIdAndUserIds.get("sysUserIds"));
                map.put("sysRoleId",sysRoleIdAndUserIds.get("sysRoleId"));
                map.put("sysRoleIdEqAndUserIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SysUserRole> sysUserRoles = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(sysUserRoles)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(sysUserRoles),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER_ROLE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, sysUserRoles, null);
        Assert.isTrue(count==sysUserRoles.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }
}
