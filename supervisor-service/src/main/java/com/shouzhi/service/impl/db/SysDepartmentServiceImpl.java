package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SysDepartmentMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysDepartment;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISysDepartmentService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统部门表业务层接口实现类
 * @author WX
 * @date 2020-11-30 12:31:11
 */
@Service("sysDepartmentService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SysDepartmentServiceImpl implements ISysDepartmentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return sysDepartmentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SysDepartment record) throws Exception {
        return sysDepartmentMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SysDepartment record) throws Exception {
        return sysDepartmentMapper.insertSelective(record);
    }

    @Override
    public SysDepartment selectByPrimaryKey(String id) {
        return sysDepartmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysDepartment record) throws Exception {
        return sysDepartmentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SysDepartment record) throws Exception {
        return sysDepartmentMapper.updateByPrimaryKey(record);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-11-30 11:43:42
     */
    @Override
    public Integer selectCount() {
        return sysDepartmentMapper.selectCount();
    }

    /**
     * 根据参数查询系统权限列表
     * @param map
     * @author WX
     * @date 2020-11-30 11:46:37
     */
    @Override
    public List<SysDepartment> queryListByPage(Map<String, Object> map) {
        return sysDepartmentMapper.queryListByPage(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-30 14:03:09
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return sysDepartmentMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-30 14:06:56
     */
    @Override
    public List<SysDepartment> BatchSelect(Map<String, Object> map) {
        return sysDepartmentMapper.BatchSelect(map);
    }

    /**
     * 查询部门树
     * @param ascriptionType
     * @param req
     * @author WX
     * @date 2020-11-30 12:44:13
     */
    @Override
    public List<TreeNodeVo> findTree(String ascriptionType, HttpServletRequest req) {
        // 查询所有部门节点
        Map<String, Object> map = new HashMap<>();
        map.put("ascriptionTypeLike", ascriptionType);
        List<SysDepartment> departments = this.queryListByPage(map);
        // 构建部门树
        return TreeNodeVo.buildTree(departments.stream().map(d -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("depType", d.getDepType());
                    jsonObject.put("ascriptionType", d.getAscriptionType());
                    jsonObject.put("parentName", d.getParentName());
                    return new TreeNodeVo(d.getId(), d.getId(), d.getParentId(), d.getDepName(), jsonObject);
                }).collect(Collectors.toList()), "0");
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-11-30 14:16:43
     */
    @Override
    public Integer save(SysDepartment record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 无父节点，
        if(StringUtils.isBlank(record.getParentId())){
            Integer selectCount = this.selectCount();
            // 数据库里存在记录，报错，必须选择父节点才可添加
            if(selectCount>0) throw new IllegalArgumentException("TREE_NOT_SELECTED_PARENT_NODE_ERROR");
            // 库里无记录，说明第一次添加，本条记录为根节点，很特殊
            record.setParentId("0");
            record.setParentIds("/0/");
            record.setDepType("/");
            record.setAscriptionType("1,2");
        } else {
            // 有父节点
            SysDepartment parentSysDepartment = this.selectByPrimaryKey(record.getParentId());
            Assert.notNull(parentSysDepartment,"DB_SQL_ID_INVALID_ERROR！");
            String parentIds = parentSysDepartment.getParentIds()+parentSysDepartment.getId()+"/";
            record.setParentIds(parentIds);
        }

        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入系统部门失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_DEPARTMENT, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-11-30 14:23:27
     */
    @Override
    public Integer update(SysDepartment record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysDepartment originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        // 跨类型不允许更改
        Assert.isTrue((originalRecord.getDepType().equals(record.getDepType()))&&(originalRecord.getAscriptionType().equals(record.getAscriptionType())),"SYS_DEP_TYPE_NOT_EQUAL_ERROR");

        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新系统资源权限失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_DEPARTMENT, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-30 14:34:49
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除部门
        BasicAuth userInfo = baseService.getUserInfo(req);
        SysDepartment record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        String cascadeId = UuidUtil.get32UUID();
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_DEPARTMENT, DBConst.OPER_TYPE_DELETE, permId, DBConst.IS_CASCADE, cascadeId, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该部门关联的子部门集(返回已删除的子部门集数据列表)
        List<SysDepartment> sysDepartments = this.batchDeleteByMultiParam("parentIdsLike", "/" + rowId + "/", permId, DBConst.IS_CASCADE, cascadeId, userInfo, false);

        // TODO 目前部门用户关联表业务没做，先注释掉
        // 获取与该部门关联的子部门集ids，将rowId合并到ids
        // List<String> collect = sysDepartments.stream().map(s -> s.getId()).collect(Collectors.toList());
        // collect.add(rowId);

        // 3.移除与该部门ids关联的用户下的部门(当删除是父部门时不仅要删除其对应的用户下的部门,更要删除父部门的所有子部门对应的用户下的部门)
        // sysDepartmentUserService.batchDeleteByMultiParam("sysDepartmentIds", collect, permId, DBConst.IS_CASCADE, cascadeId, userInfo, false);
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param sysDepartmentIds 系统部门ID，对应系统部门信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-30 14:49:39
     */
    @Override
    public Integer batchDelete(String sysDepartmentIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(sysDepartmentIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SysDepartment> sysDepartments = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(sysDepartments) && sysDepartments.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除部门
        Integer count = this.batchDelete(map);
        String cascadeId = UuidUtil.get32UUID();
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_DEPARTMENT, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.IS_CASCADE, cascadeId, userInfo, DBConst.TABLE_UNIFIED_ID, sysDepartments, null);
        Assert.isTrue(count==sysDepartments.size() && b,"DB_SQL_DELETE_ERROR");

        // 2.移除与该部门关联的子部门集(返回已删除的子部门集数据列表)
        sysDepartments = this.batchDeleteByMultiParam("parentIdsRegexp", list, permId, DBConst.IS_CASCADE, cascadeId, userInfo, false);

        // TODO 目前部门用户关联表业务没做，先注释掉
        // 获取与该部门关联的子部门集ids，将sysDepartmentsIds合并到ids
        // List<String> collect = sysDepartments.stream().map(s -> s.getId()).collect(Collectors.toList());
        // collect.addAll(list);

        // 3.批量移除与该部门关联的用户下的部门
        // sysDepartmentUserService.batchDeleteByMultiParam("sysDepartmentIds", collect, permId, DBConst.IS_CASCADE, cascadeId, userInfo, false);
        return count;
    }


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param isCascade 是否级联操作
     * @param cascadeId 级联标志
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2020-11-30 14:37:22
     */
    public List<SysDepartment> batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
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
        List<SysDepartment> sysDepartments = this.BatchSelect(map);
        if(!strictMode&& CollectionUtils.isEmpty(sysDepartments)) return sysDepartments;
        Assert.isTrue(CollectionUtils.isNotEmpty(sysDepartments),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_DEPARTMENT, DBConst.OPER_TYPE_BATCH_DELETE, permId, isCascade, cascadeId, userInfo, DBConst.TABLE_UNIFIED_ID, sysDepartments, null);
        Assert.isTrue(count==sysDepartments.size() && b,"DB_SQL_DELETE_ERROR");
        return sysDepartments;
    }

}
