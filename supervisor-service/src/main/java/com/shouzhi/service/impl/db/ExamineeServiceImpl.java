package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.ExamineeMapper;
import com.shouzhi.pojo.db.*;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.IExamVideoService;
import com.shouzhi.service.interf.db.IExaminationHallService;
import com.shouzhi.service.interf.db.IExamineeService;
import com.shouzhi.service.interf.db.ILogOperService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 考生表业务层接口实现类
 * @author WX
 * @date 2020-08-04 17:16:51
 */
@Service("examineeService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ExamineeServiceImpl implements IExamineeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExamineeMapper examineeMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private IExaminationHallService examinationHallService;

    @Autowired
    private IExamVideoService examVideoService;


    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    @Autowired
    private BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return examineeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(Examinee record) throws Exception {
        return examineeMapper.insert(record);
    }

    @Override
    public Integer insertSelective(Examinee record) throws Exception {
        return examineeMapper.insertSelective(record);
    }

    @Override
    public Examinee selectByPrimaryKey(String id) {
        return examineeMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(Examinee record) throws Exception {
        return examineeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(Examinee record) throws Exception {
        return examineeMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-04 16:58:17
     */
    @Override
    public List<Examinee> queryListByPage(Examinee record) {
        return examineeMapper.queryListByPage(record);
    }

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamineeIds列表
     * @author WX
     * @date 2020-08-05 10:26:57
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return examineeMapper.BatchUpdate(map);
    }

    /**
     * 查询考生列表，带头像，前端平台-云监考
     * @param map
     * @author WX
     * @date 2020-08-16 16:03:25
     */
    @Override
    public List<Examinee> foregroundExamineeList(Map<String, Object> map) {
        return examineeMapper.foregroundExamineeList(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 17:01:16
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return examineeMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 17:08:21
     */
    @Override
    public List<Examinee> BatchSelect(Map<String, Object> map) {
        return examineeMapper.BatchSelect(map);
    }


    /**
     * 新增考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 09:11:16
     */
    @Override
    public Integer save(Examinee record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入考生信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINEE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 新增考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 09:14:43
     */
    @Override
    public Integer saveExaminee(Examinee record, String permId, HttpServletRequest req) throws Exception {
        // 拼接推拉流地址，规则：appName+考场编号_学号
        String appName = customProperties.getProperty("live.app.name.base");
        record.setLiveAddressPush(appName+record.getExaminationHallCode()+"_"+record.getStudentCode());
        record.setLiveAddressPull(appName+record.getExaminationHallCode()+"_"+record.getStudentCode()+".flv");
        // 保存考试信息
        Integer save = this.save(record, permId, req);

        // 根据考场id 查询考场信息
        ExaminationHall examinationHall = examinationHallService.selectByPrimaryKey(record.getExaminationHallId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 新增考生视频信息，为了视频录制后能根据考场编号+学号找到记录更新视频地址等字段，那个时候再创建记录就晚了
        ExamVideo examVideo = new ExamVideo();
        examVideo.setExaminationHallId(record.getExaminationHallId());
        examVideo.setExaminationHallCode(record.getExaminationHallCode());
        examVideo.setExamineeId(record.getId());
        examVideo.setStudentCode(record.getStudentCode());
        examVideo.setVideoName(simpleDateFormat.format(examinationHall.getStartTime())+"_"+record.getStudentCode()+"_"+record.getStudentName());
        examVideoService.saveExamVideo(examVideo, permId, req);
        return save;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-08-05 10:11:28
     */
    @Override
    public Integer update(Examinee record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Examinee examinee = this.selectByPrimaryKey(record.getId());
        Assert.notNull(examinee,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新考生信息失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINEE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(examinee), JSON.toJSONString(record));
        return count;
    }

    /**
     * 修改考生信息
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-05 10:11:28
     */
    @Override
    public Integer updateExaminee(Examinee record, String permId, HttpServletRequest req) throws Exception {
        Integer update = this.update(record, permId, req);

        // 考生信息更改 将考生视频对应的学号也要更改，防止业务上根据学号处理时找不到
        Map map = new HashMap<>();
        map.put("studentCode", record.getStudentCode());
        map.put("examineeIdEq", record.getId());
        examVideoService.BatchUpdate(map);
        return update;
    }

    /**
     * 根据ID删除考生信息
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-05 10:20:43
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除考生信息
        BasicAuth userInfo = baseService.getUserInfo(req);
        Examinee record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINEE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // 2.删除考生关联的考试录制视频信息
        examVideoService.batchDeleteByMultiParam("examineeId", rowId, permId, req, true);
        return count;
    }

    /**
     * 根据IDs批量删除考生信息
     * @param examineeIds
     * @param permId      权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-05 10:20:43
     */
    @Override
    public Integer batchDelete(String examineeIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(examineeIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<Examinee> examinees = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(examinees) && examinees.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除考生信息
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINEE, DBConst.OPER_TYPE_BATCH_DELETE, DBConst.NO_CASCADE, null, permId, userInfo, DBConst.TABLE_UNIFIED_ID, examinees, null);
        Assert.isTrue(count==examinees.size() && b,"DB_SQL_DELETE_ERROR");

        // 2.批量删除考生关联的考试录制视频信息
        examVideoService.batchDeleteByMultiParam("examineeIds", list, permId, req, true);
        return count;
    }


    /**
     * 根据多参数批量删除考生信息
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-22 17:24:09
     */
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
        List<Examinee> examinees = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(examinees)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(examinees),"DB_SQL_DELETE_ERROR");
        // 1.批量删除考生信息
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAMINEE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, examinees, null);
        Assert.isTrue(count==examinees.size() && b,"DB_SQL_DELETE_ERROR");

        // 2.批量删除考生关联的考试录制视频信息
        examVideoService.batchDeleteByMultiParam(paramKey, paramVal, permId, req, true);
        return count;
    }

}
