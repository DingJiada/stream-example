package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.ExamVideoMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.ExamVideo;
import com.shouzhi.pojo.db.Examinee;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.IExamVideoService;
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
import java.util.*;

/**
 * 考试(考生)视频表业务层接口实现类
 * @author WX
 * @date 2020-08-06 17:38:49
 */
@Service("examVideoService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class ExamVideoServiceImpl implements IExamVideoService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExamVideoMapper examVideoMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    private IExamineeService examineeService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    @Autowired
    private BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return examVideoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(ExamVideo record) throws Exception {
        return examVideoMapper.insert(record);
    }

    @Override
    public Integer insertSelective(ExamVideo record) throws Exception {
        return examVideoMapper.insertSelective(record);
    }

    @Override
    public ExamVideo selectByPrimaryKey(String id) {
        return examVideoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ExamVideo record) throws Exception {
        return examVideoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(ExamVideo record) throws Exception {
        return examVideoMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询列表
     * @param record
     * @author WX
     * @date 2020-08-06 17:30:16
     */
    @Override
    public List<ExamVideo> queryListByPage(ExamVideo record) {
        return examVideoMapper.queryListByPage(record);
    }

    /**
     * 根据参数查询信息
     * @param record
     * @author WX
     * @date 2020-08-11 09:27:00
     */
    @Override
    public ExamVideo selectOneByParam(ExamVideo record) {
        return examVideoMapper.selectOneByParam(record);
    }

    /**
     * 批量更新 根据ID
     * @param map 参数+ExamVideoIds列表
     * @author WX
     * @date 2020-08-06 18:27:35
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return examVideoMapper.BatchUpdate(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-11-22 15:56:20
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return examVideoMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-11-22 16:00:32
     */
    @Override
    public List<ExamVideo> BatchSelect(Map<String, Object> map) {
        return examVideoMapper.BatchSelect(map);
    }


    /**
     * 新增考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:03:48
     */
    @Override
    public Integer save(ExamVideo record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入考试(考生)视频失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAM_VIDEO, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 新增考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:03:48
     */
    @Override
    public Integer saveExamVideo(ExamVideo record, String permId, HttpServletRequest req) throws Exception {
        Integer save = this.save(record, permId, req);
        return save;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:14:37
     */
    @Override
    public Integer update(ExamVideo record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        ExamVideo examVideo = this.selectByPrimaryKey(record.getId());
        Assert.notNull(examVideo,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新考试(考生)视频失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAM_VIDEO, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(examVideo), JSON.toJSONString(record));
        return count;
    }

    /**
     * 修改考试(考生)视频
     * @param record
     * @param permId
     * @author WX
     * @date 2020-08-06 18:14:37
     */
    @Override
    public Integer updateExamVideo(ExamVideo record, String permId, HttpServletRequest req) throws Exception {
        Integer update = this.update(record, permId, req);
        return update;
    }

    /**
     * 根据ID删除考试(考生)视频
     * @param rowId 被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:18:49
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        ExamVideo record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAM_VIDEO, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除考试(考生)视频
     * @param examVideoIds
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-08-06 18:23:10
     */
    @Override
    public Integer batchDelete(String examVideoIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(examVideoIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<ExamVideo> examVideos = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(examVideos) && examVideos.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAM_VIDEO, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, examVideos, null);
        Assert.isTrue(count==examVideos.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据多参数批量删除考试(考生)视频
     * @param paramKey 删除参数key
     * @param paramVal 删除参数val
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-11-22 16:40:21
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
            case "examineeId":
                map.put("examineeIdEq",paramVal);
                break;
            case "examineeIds":
                List<String> examineeIds =  (List<String>)paramVal;
                Assert.isTrue(CollectionUtils.isNotEmpty(examineeIds),"COMMON_INVALID_ARG_ERROR");
                map.put("list",examineeIds);
                map.put("examineeIdIn","1");
                break;
            default:
                throw new IllegalArgumentException("COMMON_INVALID_ARG_ERROR");
        }

        // 查询要删除的数据是否存在
        List<ExamVideo> examVideos = this.BatchSelect(map);
        if(!strictMode&&CollectionUtils.isEmpty(examVideos)) return 0;
        Assert.isTrue(CollectionUtils.isNotEmpty(examVideos),"DB_SQL_DELETE_ERROR");
        // 批量删除数据
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_EXAM_VIDEO, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, examVideos, null);
        Assert.isTrue(count==examVideos.size() && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 考试(考生)异常行为打点，为该考生对应的录制视频记录更新异常行为字段
     * @param examinationHallId 考试(考场)id
     * @param examineeId        考生id
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2020-08-15 19:17:26
     */
    @Override
    public Integer addExamAbnormal(String examinationHallId, String examineeId, String permId, HttpServletRequest req) throws Exception {
        // 接到请求后，查询数据库前，立马生成时间
        Date abnormalDate = new Date();
        // 根据两个id查询考试视频列表
        ExamVideo examVideo = new ExamVideo();
        examVideo.setExaminationHallId(examinationHallId);
        examVideo.setExamineeId(examineeId);
        examVideo = this.selectOneByParam(examVideo);
        Assert.notNull(examVideo,"USER_NOT_FOUND");
        // 查询考生登录时间，登录后会立马推流，所以也是推流时间
        Examinee examinee = examineeService.selectByPrimaryKey(examineeId);
        Assert.notNull(examinee.getLoginTime(),"EXAM_VIDEO_LOGIN_TIME_NULL_ERROR");
        // 更新
        String examVideoId = examVideo.getId();
        examVideo = new ExamVideo();
        examVideo.setId(examVideoId);
        examVideo.setExamAbnormal("1");
        examVideo.setAbnormalTime(abnormalDate);
        examVideo.setAbnormalMillis(String.valueOf(abnormalDate.getTime()-examinee.getLoginTime().getTime()));// 算时间
        return this.updateExamVideo(examVideo, permId, req);
    }

    /**
     * 更新考试(考生)视频观看数量
     * @param rowId
     * @author WX
     * @date 2020-08-21 15:42:36
     */
    @Override
    public Long updateWatchCount(String rowId, HttpServletRequest req) throws Exception {
        ExamVideo examVideo = this.selectByPrimaryKey(rowId);
        Assert.notNull(examVideo,"DB_SQL_ID_INVALID_ERROR！");
        Long watchCount = examVideo.getWatchCount();
        examVideo = new ExamVideo();
        examVideo.setId(rowId);
        examVideo.setWatchCount(++watchCount);
        this.updateByPrimaryKeySelective(examVideo);
        return examVideo.getWatchCount();
    }

}
