package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SysPermissionMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.MenuAndPermVo;
import com.shouzhi.pojo.vo.SysPermissionVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.DistinctWrapper;
import com.shouzhi.service.common.FileUploadService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.IShortcutMenuService;
import com.shouzhi.service.interf.db.ISysPermissionService;
import com.shouzhi.service.interf.db.ISysRolePermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统权限表业务层接口实现类
 * @author WX
 * @date 2020-06-10 09:50:10
 */
@Service("sysPermissionService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysPermissionServiceImpl implements ISysPermissionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    @Autowired
    private IShortcutMenuService shortcutMenuService;

    @Autowired
    BaseService baseService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysPermission record) throws Exception {
        return sysPermissionMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysPermission record) throws Exception {
        return sysPermissionMapper.insertSelective(record);
    }

    @Override
    public SysPermission selectByPrimaryKey(String id) {
        return sysPermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysPermission record) throws Exception {
        return sysPermissionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysPermission record) throws Exception {
        return sysPermissionMapper.updateByPrimaryKey(record);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-16 15:53:09
     */
    @Override
    public Integer selectCount() {
        return sysPermissionMapper.selectCount();
    }

    /**
     * 根据参数查询系统权限列表
     * @param map
     * @author WX
     * @date 2020-06-10 09:40:13
     */
    @Override
    public List<SysPermission> queryListByPage(Map<String, Object> map) {
        return sysPermissionMapper.queryListByPage(map);
    }

    /**
     * 批量更新根据ID
     * @param map 参数+SysPermissionIds列表
     * @author WX
     * @date 2020-11-03 10:13:26
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return sysPermissionMapper.BatchUpdate(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 10:21:19
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return sysPermissionMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 10:26:06
     */
    @Override
    public List<SysPermission> BatchSelect(Map<String, Object> map) {
        return sysPermissionMapper.BatchSelect(map);
    }


    // 这里注意,不推荐这么写！推荐Service层将异常往上抛,Controller层去捕获处理,
    // 因为Service层捕获异常后事务会默认失效,新手容易跳坑
    // 这里仍然这样写的原因是保留手动回滚代码样本,供长久参考
    @Override
    public CommonResult<String> save(SysPermission record, MultipartFile imgFile, String permId, HttpServletRequest req) {
        CommonResult<String> result = new CommonResult<>();
        try {
            BasicAuth userInfo = baseService.getUserInfo(req);
            // 无父节点，
            if(StringUtils.isBlank(record.getParentId())){
                Integer selectCount = this.selectCount();
                // 数据库里存在记录，报错，必须选择父节点才可添加
                if(selectCount>0) throw new IllegalArgumentException("TREE_NOT_SELECTED_PARENT_NODE_ERROR");
                // 库里无记录，说明第一次添加，本条记录为根节点，很特殊
                record.setType("/");
                record.setUrl("/");
                record.setPercode("/");
                record.setParentId("0");
                record.setParentIds("/0/");
                record.setAscriptionType("1,2");
            } else {
                // 有父节点
                SysPermission parentSysPermission = this.selectByPrimaryKey(record.getParentId());
                Assert.notNull(parentSysPermission,"DB_SQL_ID_INVALID_ERROR！");
                String parentIds = parentSysPermission.getParentIds()+parentSysPermission.getId()+"/";
                record.setParentIds(parentIds);
            }

            // 上传图片
            if(imgFile!=null&&!imgFile.isEmpty()){
                String typePath = customProperties.getProperty("file.uploaded.icons.path");
                String customPath = customProperties.getProperty("file.uploaded.icons.menu.path");
                String uploadPath = fileUploadService.uploadImg(imgFile, typePath, customPath);
                record.setIconUrl(uploadPath);
            }

            record.setId(UuidUtil.get32UUID());
            record.setCreateId(userInfo.getId());
            record.setCreateBy(userInfo.getUserName());
            Integer count = this.insertSelective(record);
            Assert.isTrue(count==1,"插入系统资源权限失败！");
            // 插入操作日志
            logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_PERMISSION, DBConst.OPER_TYPE_INSERT,
                    permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
            result.setStatus(1).setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.DB_SQL_INSERT_ERROR);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//进行手动回滚
        } finally {
            return result;
        }
    }

    /**
     * 根据ID更新系统资源权限
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-11 16:01:25
     */
    @Override
    public Integer update(SysPermission record, MultipartFile imgFile, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysPermission originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        // 跨类型不允许更改
        Assert.isTrue((originalRecord.getType().equals(record.getType()))&&(originalRecord.getAscriptionType().equals(record.getAscriptionType())),"SYS_PERM_TYPE_NOT_EQUAL_ERROR");

        // 上传图片
        if(imgFile!=null&&!imgFile.isEmpty()){
            String typePath = customProperties.getProperty("file.uploaded.icons.path");
            String customPath = customProperties.getProperty("file.uploaded.icons.menu.path");
            String uploadPath = fileUploadService.uploadImg(imgFile, typePath, customPath);
            record.setIconUrl(uploadPath);
        }

        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新系统资源权限失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_PERMISSION, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除系统资源权限
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-06-11 17:26:27
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除菜单
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysPermission record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_PERMISSION, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该菜单关联的子菜单集(返回已删除的子菜单集数据列表)
        List<SysPermission> sysPermissions = this.batchDeleteByMultiParam("parentIdsLike", "/" + rowId + "/", permId, userInfo, false);

        // 获取与该菜单关联的子菜单集ids，将rowId合并到ids
        List<String> collect = sysPermissions.stream().map(s -> s.getId()).collect(Collectors.toList());
        collect.add(rowId);

        // 3.移除与该菜单ids关联的角色下的权限(当删除是父菜单时不仅要删除其对应的角色下的权限,更要删除父菜单的所有子菜单对应的角色下的权限)
        sysRolePermissionService.batchDeleteByMultiParam("sysPermissionIds", collect, permId, userInfo, false);

        // 4.移除与该菜单ids关联的用户下的快捷菜单(当删除是父菜单时要删除父菜单的所有子菜单对应的用户下的快捷菜单)
        shortcutMenuService.batchDeleteByMultiParam("sysPermissionIds", collect, permId, userInfo, false);
        return count;
    }

    /**
     * 根据IDs批量删除系统资源权限
     * @param sysPermissionIds 系统资源权限ID，对应系统资源权限信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId           权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-03 10:03:23
     */
    @Override
    public Integer batchDelete(String sysPermissionIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(sysPermissionIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SysPermission> sysPermissions = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(sysPermissions) && sysPermissions.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除菜单
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_PERMISSION, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, sysPermissions, null);
        Assert.isTrue(count==sysPermissions.size() && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该菜单关联的子菜单集(返回已删除的子菜单集数据列表)
        sysPermissions = this.batchDeleteByMultiParam("parentIdsRegexp", list, permId, userInfo, false);

        // 获取与该菜单关联的子菜单集ids，将sysPermissionIds合并到ids
        List<String> collect = sysPermissions.stream().map(s -> s.getId()).collect(Collectors.toList());
        collect.addAll(list);

        // 3.批量移除与该菜单关联的角色下的权限
        sysRolePermissionService.batchDeleteByMultiParam("sysPermissionIds", collect, permId, userInfo, false);

        // 4.批量移除与该菜单ids关联的用户下的快捷菜单(当删除是父菜单时要删除父菜单的所有子菜单对应的用户下的快捷菜单)
        shortcutMenuService.batchDeleteByMultiParam("sysPermissionIds", collect, permId, userInfo, false);
        return count;
    }


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-26 14:16:39
     */
    public List<SysPermission> batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, BasicAuth userInfo, boolean strictMode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "parentIdsLike":
                map.put("parentIdsLike",paramVal);
                break;
            case "parentIdsRegexp":
                List<String> parentIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(parentIds),"COMMON_INVALID_ARG_ERROR");
                map.put("parentIdsRegexp",parentIds.stream().map(s-> String.join(s,"/","/")).collect(Collectors.joining("|")));
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<SysPermission> sysPermissions = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(sysPermissions)) return sysPermissions;
        Assert.isTrue(CollectionUtils.isNotEmpty(sysPermissions),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_PERMISSION, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, sysPermissions, null);
        Assert.isTrue(count==sysPermissions.size() && b,"DB_SQL_DELETE_ERROR");
        return sysPermissions;
    }

    /**
     * 获取用户菜单列表和权限列表
     * @param req
     * @author WX
     * @date 2021-01-19 15:10:27
     */
    @Override
    public MenuAndPermVo getMenuAndPerms(HttpServletRequest req) throws Exception {
        // 根据用户获取菜单
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysUser sysUser = userInfo.getSysUser();
        // 获取当前登录用户的所有角色(角色内存有菜单)
        List<SysPermission> permissions = sysUser.getSysRoles().stream().map(SysRole::getSysPermissions).flatMap(Collection::stream).collect(Collectors.toList());
        // 去重SysPermission,拿到多个角色唯一菜单列表，防止一人对应多个角色时出现菜单重复的现象(不在上边stream直接去重的原因是多个角色的情况少，存在时再去重，提高效率)
        if(sysUser.getSysRoles().size()>1){
            List<SysPermission> collect = permissions.stream().filter(DistinctWrapper.byKey(SysPermission::getId)).collect(Collectors.toList());
            permissions.clear();
            permissions.addAll(collect);
            collect.clear();
        }
        // 分别构造 前台菜单列表+权限列表、后台菜单列表+权限列表 并返回
        MenuAndPermVo menuAndPermVo = new MenuAndPermVo();
        List<SysPermission> bgPermissions = permissions.stream().filter(s -> "1".equals(s.getAscriptionType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(bgPermissions)){
            menuAndPermVo.setHasBgPerms("1");
            menuAndPermVo.setBgMenus(this.getPermissions(bgPermissions));
            menuAndPermVo.setBgPermCodes(this.getPermCodes(bgPermissions));
            bgPermissions.clear();
            bgPermissions=null;
        }
        List<SysPermission> fgPermissions = permissions.stream().filter(s -> "2".equals(s.getAscriptionType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(fgPermissions)){
            menuAndPermVo.setHasFgPerms("1");
            menuAndPermVo.setFgMenus(this.getPermissions(fgPermissions));
            menuAndPermVo.setFgPermCodes(this.getPermCodes(fgPermissions));
            fgPermissions.clear();
            fgPermissions=null;
        }
        permissions.clear();
        permissions=null;
        return menuAndPermVo;
    }

    /**
     * 获取用户菜单列表
     * @param sysPermissions
     * @author WX
     * @date 2020-06-15 16:39:51
     */
    @Override
    public List<SysPermissionVo> getPermissions(List<SysPermission> sysPermissions) throws Exception {
        List<SysPermissionVo> spvs = new ArrayList<>();
        if(!sysPermissions.isEmpty()){
            // 获取一级菜单(一开始用的"1".equals(sysPermission.getParentId())，但id并不固定为1，之后改为用getType)
            spvs = sysPermissions.stream().filter(sysPermission -> "1".equals(sysPermission.getType()))
                    .map(sp -> new SysPermissionVo(sp.getId(), sp.getName(), sp.getType(), sp.getUrl(), sp.getPercode(), sp.getParentId(), sp.getParentIds(), sp.getSortNum(), sp.getIconStyle(), sp.getIconUrl(), new ArrayList<>()))
                    .collect(Collectors.toList());
            // 将list转为Map
            Map<String, SysPermissionVo> voMap = spvs.stream().collect(Collectors.toMap(spv -> spv.getId(), spv -> spv));
            Map<String, SysPermissionVo> secondVoMap = new HashMap<>();
            // 二级菜单
            sysPermissions.stream().forEach(sp ->
                    Optional.ofNullable(voMap.get(sp.getParentId())).ifPresent(spv -> {
                            SysPermissionVo secondVo = new SysPermissionVo(sp.getId(), sp.getName(), sp.getType(), sp.getUrl(), sp.getPercode(), sp.getParentId(), sp.getParentIds(), sp.getSortNum(), sp.getIconStyle(), sp.getIconUrl(), new ArrayList<>());
                            spv.getChildren().add(secondVo);
                            secondVoMap.put(secondVo.getId(), secondVo);
                        })
            );
            // // button
            // sysPermissions.stream().forEach(sp ->
            //         Optional.ofNullable(secondVoMap.get(sp.getParentId())).ifPresent(spv ->
            //                 spv.getChildren().add(new SysPermissionVo(sp.getId(), sp.getName(), sp.getType(), sp.getUrl(), sp.getPercode(), sp.getParentId(), sp.getParentIds(), sp.getSortNum(), sp.getIconStyle(), sp.getIconUrl(), null))));
        }
        return spvs;
    }

    /**
     * 获取用户权限列表
     * @param sysPermissions
     * @author WX
     * @date 2021-01-19 15:10:27
     */
    @Override
    public List<String> getPermCodes(List<SysPermission> sysPermissions) throws Exception {
        return sysPermissions.parallelStream().map(SysPermission::getPercode).collect(Collectors.toList());
    }



    /**
     * 查询菜单树
     * 备注：此树编写的比较早，没有用到TreeNodeVo.buildTree，属于保留代码；
     *      当不再满足新业务需求时可以注释掉直接使用TreeNodeVo.buildTree一键生成菜单树；
     * @param ascriptionType
     * @param req
     * @author WX
     * @date 2020-10-30 10:10:23
     */
    @Override
    public List<TreeNodeVo> findTree(String ascriptionType, HttpServletRequest req) {
        // 查询所有菜单节点
        Map<String, Object> map = new HashMap<>();
        map.put("ascriptionTypeLike", ascriptionType);
        List<SysPermission> permissions = sysPermissionMapper.queryListByPage(map);
        List<TreeNodeVo> treeNodeVos = new ArrayList<>();
        // 构建菜单树
        if(!permissions.isEmpty()){
            // 获取根菜单
            TreeNodeVo rootNodeVo = permissions.stream().filter(sysPermission -> "0".equals(sysPermission.getParentId()))
                    .map(sp -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", sp.getType());
                        jsonObject.put("ascriptionType", sp.getAscriptionType());
                        jsonObject.put("parentName", sp.getParentName());
                        return new TreeNodeVo(sp.getId(), sp.getId(), sp.getParentId(), sp.getName(), jsonObject);
                    }).findAny().get();
            // 获取一级菜单
            permissions.stream().filter(sysPermission -> rootNodeVo.getId().equals(sysPermission.getParentId()))
                    .forEach(sp -> {
                        rootNodeVo.setIsLeaf(0);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", sp.getType());
                        jsonObject.put("ascriptionType", sp.getAscriptionType());
                        jsonObject.put("parentName", sp.getParentName());
                        rootNodeVo.getChildren().add(new TreeNodeVo(sp.getId(), sp.getId(), sp.getParentId(), sp.getName(), jsonObject));
                    });
            // 将一级菜单list转为Map
            Map<String, TreeNodeVo> voMap = rootNodeVo.getChildren().stream().collect(Collectors.toMap(tn -> tn.getId(), tn -> tn));
            Map<String, TreeNodeVo> secondVoMap = new HashMap<>();
            // 二级菜单
            permissions.stream().forEach(sp ->
                    Optional.ofNullable(voMap.get(sp.getParentId())).ifPresent(tn -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", sp.getType());
                        jsonObject.put("ascriptionType", sp.getAscriptionType());
                        jsonObject.put("parentName", sp.getParentName());
                        TreeNodeVo secondVo = new TreeNodeVo(sp.getId(), sp.getId(), sp.getParentId(), sp.getName(), jsonObject);
                        tn.setIsLeaf(0);
                        tn.getChildren().add(secondVo);
                        secondVoMap.put(secondVo.getId(), secondVo);
                    })
            );
            Map<String, TreeNodeVo> buttonVoMap = new HashMap<>();
            // button
            permissions.stream().forEach(sp ->
                    Optional.ofNullable(secondVoMap.get(sp.getParentId())).ifPresent(tn -> {
                        tn.setIsLeaf(0);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", sp.getType());
                        jsonObject.put("ascriptionType", sp.getAscriptionType());
                        jsonObject.put("parentName", sp.getParentName());
                        TreeNodeVo buttonVo = new TreeNodeVo(sp.getId(), sp.getId(), sp.getParentId(), sp.getName(), jsonObject);
                        tn.getChildren().add(buttonVo);
                        buttonVoMap.put(buttonVo.getId(), buttonVo);
                    }));
            // btn_menu
            permissions.stream().forEach(sp ->
                    Optional.ofNullable(buttonVoMap.get(sp.getParentId())).ifPresent(tn -> {
                        tn.setIsLeaf(0);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", sp.getType());
                        jsonObject.put("ascriptionType", sp.getAscriptionType());
                        jsonObject.put("parentName", sp.getParentName());
                        tn.getChildren().add(new TreeNodeVo(sp.getId(), sp.getId(), sp.getParentId(), sp.getName(), jsonObject));
                    }));
            treeNodeVos.add(rootNodeVo);
        }
        return treeNodeVos;
    }
}
