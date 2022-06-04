package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.ExaminationHallMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ExaminationHall;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 考场考试表业务层接口实现类
 * @author WX
 * @date 2020-08-03 12:45:22
 */
@Service("examinationHallService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ExaminationHallServiceImpl implements IExaminationHallService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExaminationHallMapper examinationHallMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private IExaminationHallInvigilatorsService examinationHallInvigilatorsService;

    @Autowired
    private IExamineeService examineeService;

    @Autowired
    private IExamVideoService examVideoService;

    @Autowired
    private BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return examinationHallMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(ExaminationHall record) throws Exception {
        return examinationHallMapper.insert(record);
    }

    @Override
    public Integer insertSelective(ExaminationHall record) throws Exception {
        return examinationHallMapper.insertSelective(record);
    }

    @Override
    public ExaminationHall selectByPrimaryKey(String id) {
        return examinationHallMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ExaminationHall record) throws Exception {
        return examinationHallMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(ExaminationHall record) throws Exception {
        return examinationHallMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-03 12:49:23
     */
    @Override
    public List<ExaminationHall> queryListByPage(ExaminationHall record) {
        return examinationHallMapper.queryListByPage(record);
    }

    /**
     * 根据用户表的参数字段查询考试信息，比如：根据用户ID、根据用户姓名等，不涉及groupBy
     * @param map
     * @author WX
     * @date 2020-08-05 18:43:18
     */
    @Override
    public List<ExaminationHall> selectByUserParam(Map<String, Object> map) {
        return examinationHallMapper.selectByUserParam(map);
    }

    /**
     * 批量更新 根据ID
     * @param map 参数+ExaminationHallIds列表
     * @author WX
     * @date 2020-08-04 14:08:42
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return examinationHallMapper.BatchUpdate(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:46:09
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return examinationHallMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:51:39
     */
    @Override
    public List<ExaminationHall> BatchSelect(Map<String, Object> map) {
        return examinationHallMapper.BatchSelect(map);
    }

    /**
     * 新增考试
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-03 15:17:43
     */
    @Override
    public Integer save(ExaminationHall record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入考试信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 新增考试+监考老师信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-03 15:26:08
     */
    @Override
    public Integer saveExamAndInvigilators(ExaminationHall record, String permId, HttpServletRequest req) throws Exception {
        // 保存考试信息
        Integer save = this.save(record, permId, req);
        // 插入考试-监考表
        examinationHallInvigilatorsService.batchSaveByExaminationHallId(record.getId(), record.getInvigilatorIds(), permId, req);
        return save;
    }


    /**
     * 根据ID更新考试信息
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-06-15 09:50:52
     */
    @Override
    public Integer update(ExaminationHall record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        ExaminationHall examinationHall = this.selectByPrimaryKey(record.getId());
        Assert.notNull(examinationHall,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新考试信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(examinationHall), JSON.toJSONString(record));
        return count;
    }


    /**
     * 修改考试+监考老师信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-04 09:04:16
     */
    @Override
    public Integer updateExamAndInvigilators(ExaminationHall record, String permId, HttpServletRequest req) throws Exception {
        Integer update = this.update(record, permId, req);
        // 根据ExaminationHallId删除关联的考试-监考用户信息
        examinationHallInvigilatorsService.batchDeleteByMultiParam("examinationHallId", record.getId(), permId, req, true);
        // 插入考试-监考表
        examinationHallInvigilatorsService.batchSaveByExaminationHallId(record.getId(), record.getInvigilatorIds(), permId, req);

        // 考场信息更改 将考生信息、考生视频对应的考场编号也要更改，防止业务上根据学号处理时找不到
        Map map = new HashMap<>();
        map.put("examinationHallCode", record.getHallCode());
        map.put("examinationHallIdEq", record.getId());
        examineeService.BatchUpdate(map);
        examVideoService.BatchUpdate(map);
        return update;
    }

    /**
     * 根据ID删除考试信息
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-04 13:43:24
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除考试信息
        BasicAuth userInfo = baseService.getUserInfo(req);
        ExaminationHall record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        // 2.这里考试-监考关联表的记录没动，保留
        // 3.删除考生信息
        examineeService.batchDeleteByMultiParam("examinationHallId", rowId, permId, req, true);
        return count;
    }

    /**
     * 根据IDs批量删除考试信息
     * @param examinationHallIds
     * @param permId             权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-04 13:59:16
     */
    @Override
    public Integer batchDelete(String examinationHallIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(examinationHallIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<ExaminationHall> examinationHalls = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(examinationHalls) && examinationHalls.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINATION_HALL, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, examinationHalls, null);
        Assert.isTrue(count==examinationHalls.size() && b,"DB_SQL_DELETE_ERROR");
        // 2.这里考试-监考关联表的记录没动，保留
        // 3.批量删除考生信息
        examineeService.batchDeleteByMultiParam("examinationHallIds", list, permId, req, true);
        return count;
    }









    /*=============================================考场视频专辑===================================================*/

    /**
     * 删除专辑
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-07 10:08:49
     */
    @Override
    public Integer deleteAlbum(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 删除
        ExaminationHall examinationHall = new ExaminationHall();
        examinationHall.setId(rowId);
        examinationHall.setShowAlbum("0");
        this.update(examinationHall, permId, req, DBConst.OPER_TYPE_DELETE);
        // 删除考生关联的考试录制视频信息
        examVideoService.batchDeleteByMultiParam("examinationHallId", rowId, permId, req, true);
        return 1;
    }

    /**
     * 批量删除专辑
     * @param examinationHallIds
     * @param permId             权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-07 10:09:28
     */
    @Override
    public Integer batchDeleteAlbum(String examinationHallIds, String permId, HttpServletRequest req) throws Exception {
        List<String> list = Arrays.asList(examinationHallIds.split(","));
        Map map = new HashMap<>();
        map.put("showAlbum","0");
        map.put("list",list);
        this.BatchUpdate(map);
        // 批量删除考生关联的考试录制视频信息
        examVideoService.batchDeleteByMultiParam("examinationHallIds", list, permId, req, true);
        return 1;
    }
}
