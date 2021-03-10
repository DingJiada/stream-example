package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SysRolePermissionMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestionSet;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.db.SysRolePermission;
import com.shouzhi.pojo.po.SysPermissionPo;
import com.shouzhi.pojo.vo.SysPermissionSettingVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISysPermissionService;
import com.shouzhi.service.interf.db.ISysRolePermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 系统角色-资源权限表业务层接口实现类
 * @author WX
 * @date 2020-06-12 13:54:18
 */
@Service("sysRolePermissionService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysRolePermissionServiceImpl implements ISysRolePermissionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysRolePermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysRolePermission record) throws Exception {
        return sysRolePermissionMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysRolePermission record) throws Exception {
        return sysRolePermissionMapper.insertSelective(record);
    }

    @Override
    public SysRolePermission selectByPrimaryKey(String id) {
        return sysRolePermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysRolePermission record) throws Exception {
        return sysRolePermissionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysRolePermission record) throws Exception {
        return sysRolePermissionMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询系统角色-资源权限列表
     * @param record
     * @author WX
     * @date 2020-06-12 11:27:36
     */
    @Override
    public List<SysRolePermission> queryListByPage(SysRolePermission record) {
        return sysRolePermissionMapper.queryListByPage(record);
    }

    /**
     * 根据RoleId查询所有资源权限信息
     * @param roleId
     * @author WX
     * @date 2020-06-12 13:45:06
     */
    @Override
    public List<SysPermission> selectPermissionsByRoleId(String roleId) {
        return sysRolePermissionMapper.selectPermissionsByRoleId(roleId);
    }

    /**
     * 查询多级菜单全部信息
     * @author WX
     * @date 2020-08-25 15:06:13
     */
    @Override
    public List<SysPermissionPo> multiLevelPermissions() {
        return sysRolePermissionMapper.multiLevelPermissions();
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 10:40:15
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return sysRolePermissionMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 10:46:21
     */
    @Override
    public List<SysRolePermission> BatchSelect(Map<String, Object> map) {
        return sysRolePermissionMapper.BatchSelect(map);
    }


    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-26 13:46:10
     */
    @Override
    public Integer batchInsert(List<SysRolePermission> list) {
        return sysRolePermissionMapper.batchInsert(list);
    }

    /**
     * 根据RoleId查询资源权限设置列表，非树型显示方式，平铺的显示方式
     * 注意：本代码有BUG，已经废弃，不能显示一级菜单，只能显示二级菜单，向“首页”这种无二级菜单的就无法赋权限
     *      此展示方式本来就弊大于利，后端费劲，前端更费劲，没有对应的组件需要自己实现，
     * @param roleId
     * @author WX
     * @date 2020-06-12 13:45:06
     */
    /*@Override
    public List<SysPermissionSettingVo> selectPermissionSettingList(String roleId) {
        //  查询所有菜单集合
        List<SysPermissionPo> sysPermissionPos = this.multiLevelPermissions();
        //  查询某个角色下已挂载的菜单
        List<SysPermission> sysPermissions = this.selectPermissionsByRoleId(roleId);
        Map<String, SysPermission> spMap = sysPermissions.stream().collect(Collectors.toMap(sp -> sp.getId(), sp -> sp));
        //  去重 拿到所有二级菜单id
        // List<SysPermissionPo> collect = sysPermissionPos.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysPermissionPo::getLevel2Id))), ArrayList::new));
        List<SysPermissionPo> collect = sysPermissionPos.stream().filter(s -> s!=null).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysPermissionPo::getLevel2Id))), ArrayList::new));
        //  遍历是否选中
        // 过滤没有按钮的菜单，就是只创建了菜单，但没创建该菜单下的权限按钮，比如最基础的查询
        List<SysPermissionSettingVo> settingVos = collect.stream().filter(spp -> spp.getBtnId()!=null).map(spp -> {
            AtomicReference<Integer> count = new AtomicReference<>(0);
            // List<SysPermissionSettingVo> children = sysPermissionPos.stream().filter(sysPermissionPo -> spp.getLevel2Id().equals(sysPermissionPo.getBtnParentId()))
            List<SysPermissionSettingVo> children = sysPermissionPos.stream().filter(sysPermissionPo -> sysPermissionPo!=null && spp.getLevel2Id().equals(sysPermissionPo.getBtnParentId()))
                    .map(sysPermissionPo2 -> {
                        SysPermissionSettingVo spsv = new SysPermissionSettingVo(sysPermissionPo2.getBtnId(), sysPermissionPo2.getBtnName(), sysPermissionPo2.getBtnType(), sysPermissionPo2.getBtnParentId(), "0", null);
                        if(spMap.get(sysPermissionPo2.getBtnId())!=null){
                            count.getAndSet(count.get() + 1);
                            spsv.setIsSelected("1");
                        }
                        return spsv;
                    })
                    .collect(Collectors.toList());
            SysPermissionSettingVo sysPermissionSettingVo = new SysPermissionSettingVo(spp.getLevel2Id(), spp.getLevel2Name(), spp.getLevel2Type(), spp.getLevel2ParentId(), "0", children);
            if(count.get()==children.size()){
                sysPermissionSettingVo.setIsSelected("1");
            }
            return sysPermissionSettingVo;
        }).collect(Collectors.toList());
        // TODO 后期查询较慢，利用redis缓存只查一次数据库，提高效率
        return settingVos;
    }*/


    /**
     * 根据RoleId查询资源权限设置列表
     * @param roleId
     * @author WX
     * @date 2021-01-14 16:18:23
     */
    @Override
    public List<TreeNodeVo> selectPermissionSettingList(String roleId) {
        //  查询所有菜单集合
        List<SysPermission> permissions = sysPermissionService.queryListByPage(new HashMap<>());
        List<String> expandedKeys = permissions.parallelStream().filter(s -> "1".equals(s.getType())).map(SysPermission::getId).collect(Collectors.toList());
        //  查询某个角色下已挂载的菜单
        List<SysPermission> sysPermissions = this.selectPermissionsByRoleId(roleId);
        List<String> permIds = sysPermissions.parallelStream().map(SysPermission::getId).collect(Collectors.toList());
        return TreeNodeVo.buildTree(
                permissions.stream().map(p -> {
                    TreeNodeVo treeNodeVo = new TreeNodeVo(p.getId(), p.getId(), p.getParentId(), p.getName());
                    // 将已挂载的菜单ids、expandedKeys放在根节点的Attributes，用于前端element设置默认选中和设置默认展开
                    if("0".equals(p.getParentId())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("defaultCheckedKeys", permIds);
                        jsonObject.put("defaultExpandedKeys", expandedKeys);
                        treeNodeVo.setAttributes(jsonObject);
                        treeNodeVo.setDisabled(true);
                    }
                    return treeNodeVo;
                }).collect(Collectors.toList()),
                "0");
    }


    /**
     * 批量保存系统角色-资源权限
     * @param sysRoleId     系统角色ID
     * @param permId        权限ID或菜单ID(仅限于最后级别的菜单)
     * @param permissionIds 资源权限ID列表集合
     * @author WX
     * @date 2020-06-12 15:06:29
     */
    @Override
    public Integer batchSave(String sysRoleId, String permId, List<String> permissionIds, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 根据sysRoleId删除
        this.batchDeleteByMultiParam("sysRoleId", sysRoleId, permId, userInfo, false);
        // 仅仅是取消全部菜单权限，直接返回
        if(CollectionUtils.isEmpty(permissionIds)) return 0;
        List<SysRolePermission> sysRolePermissions = permissionIds.stream().distinct().map(s -> new SysRolePermission(UuidUtil.get32UUID(), sysRoleId, s, userInfo.getId(), userInfo.getUserName(), null, null)).collect(Collectors.toList());
        //  批量插入
        Integer count = this.batchInsert(sysRolePermissions);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_ROLE_PERMISSION, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, sysRolePermissions);
        Assert.isTrue(count==permissionIds.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-23 10:44:16
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "sysPermissionId":
                map.put("sysPermissionIdEq",paramVal);
                break;
            case "sysRoleId":
                map.put("sysRoleIdEq",paramVal);
                break;
            case "sysPermissionIds":
                List<String> sysPermissionIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(sysPermissionIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",sysPermissionIds);
                map.put("sysPermissionIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SysRolePermission> sysRolePermissions = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(sysRolePermissions)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(sysRolePermissions),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_ROLE_PERMISSION, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, sysRolePermissions, null);
        Assert.isTrue(count==sysRolePermissions.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }
}
