package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.ExaminationHallInvigilatorsMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ExamVideo;
import com.shouzhi.pojo.db.ExaminationHallInvigilators;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.IExaminationHallInvigilatorsService;
import com.shouzhi.service.interf.db.ILogOperService;
import org.apache.commons.collections.CollectionUtils;
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
 * 考场考试-监考老师关联表业务层接口实现类
 * @author WX
 * @date 2020-08-03 14:13:14
 */
@Service("examinationHallInvigilatorsService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ExaminationHallInvigilatorsServiceImpl implements IExaminationHallInvigilatorsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExaminationHallInvigilatorsMapper examinationHallInvigilatorsMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return examinationHallInvigilatorsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(ExaminationHallInvigilators record) throws Exception {
        return examinationHallInvigilatorsMapper.insert(record);
    }

    @Override
    public Integer insertSelective(ExaminationHallInvigilators record) throws Exception {
        return examinationHallInvigilatorsMapper.insertSelective(record);
    }

    @Override
    public ExaminationHallInvigilators selectByPrimaryKey(String id) {
        return examinationHallInvigilatorsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ExaminationHallInvigilators record) throws Exception {
        return examinationHallInvigilatorsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(ExaminationHallInvigilators record) throws Exception {
        return examinationHallInvigilatorsMapper.updateByPrimaryKey(record);
    }

    /**
     * 批量插入
     * @param list
     * @author WX
     * @date 2020-08-03 13:57:19
     */
    @Override
    public Integer batchInsert(List<ExaminationHallInvigilators> list) {
        return examinationHallInvigilatorsMapper.batchInsert(list);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-23 17:13:18
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return examinationHallInvigilatorsMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-23 17:18:27
     */
    @Override
    public List<ExaminationHallInvigilators> BatchSelect(Map<String, Object> map) {
        return examinationHallInvigilatorsMapper.BatchSelect(map);
    }

    /**
     * 批量插入根据单个ExaminationHallId，含插入操作日志
     * @param examinationHallId
     * @param sysUserIds
     * @param permId
     * @author WX
     * @date 2020-08-03 15:40:43
     */
    @Override
    public Integer batchSaveByExaminationHallId(String examinationHallId, String sysUserIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 组建ExaminationHallInvigilators
        List<ExaminationHallInvigilators> examinationHallInvigilators = this.convertExaminationHallInvigilators(examinationHallId, sysUserIds, userInfo);
        // 批量插入
        Integer count = this.batchInsert(examinationHallInvigilators);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL_INVIGILATORS, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, examinationHallInvigilators);
        Assert.isTrue(count==examinationHallInvigilators.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    private List<ExaminationHallInvigilators> convertExaminationHallInvigilators(String examinationHallId, String sysUserIds, BasicAuth userInfo){
        return Arrays.stream(sysUserIds.split(",")).map(sysUserId -> {
            ExaminationHallInvigilators examinationHallInvigilators = new ExaminationHallInvigilators();
            examinationHallInvigilators.setExaminationHallId(examinationHallId);
            examinationHallInvigilators.setSysUserId(sysUserId);
            examinationHallInvigilators.setId(UuidUtil.get32UUID());
            examinationHallInvigilators.setCreateId(userInfo.getId());
            examinationHallInvigilators.setCreateBy(userInfo.getUserName());
            return examinationHallInvigilators;
        }).collect(Collectors.toList());
    }

    /**
     * 批量插入根据多个ExaminationHallId，含插入操作日志
     * TODO 这个目前还没做，因为跟之前的用户角色不一样，不是每个用户对应相同的角色，这里是每个考场的每个监考老师用户列表不一样！！
     * @param examinationHallIds
     * @param sysUserIds
     * @param permId
     * @author WX
     * @date 2020-08-03 15:40:43
     */
    /*@Override
    public Integer batchSaveByExaminationHallIds(String examinationHallIds, String sysUserIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        Arrays.stream(sysUserIds.split(",")).forEach(sysUserId -> sysUserRoles.addAll(this.convertSysUserRoles(sysUserId, sysRoleIds, userInfo)));
        // 批量插入角色
        Integer count = this.batchInsert(sysUserRoles);
        Assert.isTrue(count==sysUserRoles.size(),"批量插入系统用户角色关联ID失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SYS_USER_ROLE, DBConst.OPER_TYPE_INSERT,
                permId, userInfo, "batchInsert", null, JSON.toJSONString(sysUserRoles));
        return count;
    }*/


    /**
     * 根据多参数批量删除
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-23 17:16:06
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, HttpServletRequest req, boolean strictMode) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Map<String, Object> map = new HashMap<>();
        switch (paramKey){
            case "examinationHallId":
                map.put("examinationHallIdEq",paramVal);
                break;
            case "examinationHallIds":
                List<String> examinationHallIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(examinationHallIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",examinationHallIds);
                map.put("examinationHallIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<ExaminationHallInvigilators> examinationHallInvigilators = this.BatchSelect(map);
        if(!strictMode&& CollectionUtils.isEmpty(examinationHallInvigilators)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(examinationHallInvigilators),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL_INVIGILATORS, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, examinationHallInvigilators, null);
        Assert.isTrue(count==examinationHallInvigilators.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

}
