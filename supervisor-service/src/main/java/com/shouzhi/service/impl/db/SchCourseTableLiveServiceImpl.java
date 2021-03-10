package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchCourseTableBaseMapper;
import com.shouzhi.mapper.SchCourseTableLiveMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.db.SchCourseTableLive;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校直播课程表表业务层接口实现类
 * @author WX
 * @date 2021-02-23 14:20:28
 */
@Service("schCourseTableLiveService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchCourseTableLiveServiceImpl implements ISchCourseTableLiveService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchCourseTableLiveMapper schCourseTableLiveMapper;

    @Autowired
    private ISchDeviceService schDeviceService;

    @Autowired
    private ISchSemesterService schSemesterService;

    @Autowired
    private ISchSpaceService schSpaceService;

    @Autowired
    private IServerHostService serverHostService;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;


    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schCourseTableLiveMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchCourseTableLive record) throws Exception {
        return schCourseTableLiveMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchCourseTableLive record) throws Exception {
        return schCourseTableLiveMapper.insertSelective(record);
    }

    @Override
    public SchCourseTableLive selectByPrimaryKey(String id) {
        return schCourseTableLiveMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchCourseTableLive record) throws Exception {
        return schCourseTableLiveMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchCourseTableLive record) throws Exception {
        return schCourseTableLiveMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2021-02-23 10:49:18
     */
    @Override
    public SchCourseTableLive selectOneByParam(Map<String, Object> map) {
        return schCourseTableLiveMapper.selectOneByParam(map);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2021-02-23 10:51:26
     */
    @Override
    public Integer selectCount() {
        return schCourseTableLiveMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2021-02-23 10:54:29
     */
    @Override
    public List<SchCourseTableLive> queryListByPage(Map<String, Object> map) {
        return schCourseTableLiveMapper.queryListByPage(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2021-02-23 10:58:37
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schCourseTableLiveMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2021-02-23 11:06:18
     */
    @Override
    public List<SchCourseTableLive> BatchSelect(Map<String, Object> map) {
        return schCourseTableLiveMapper.BatchSelect(map);
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2021-02-23 14:18:03
     */
    @Override
    public Integer save(SchCourseTableLive record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校直播课程表失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @param operType
     * @author WX
     * @date 2021-02-23 14:23:16
     */
    @Override
    public Integer update(SchCourseTableLive record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseTableLive originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校直播课程表失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除
     * @param rowId  被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2021-02-23 14:25:30
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseTableLive record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");

        // TODO 级联删除对应的 评论 课件 录制视频
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param schCourseTableLiveIds 学校直播课程表ID，对应学校直播课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId                权限ID或菜单ID(仅限于最后级别的菜单)
     * @param req
     * @author WX
     * @date 2021-02-23 14:29:19
     */
    @Override
    public Integer batchDelete(String schCourseTableLiveIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schCourseTableLiveIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchCourseTableLive> schCourseTableLives = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schCourseTableLives) && schCourseTableLives.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schCourseTableLives, null);
        Assert.isTrue(count== schCourseTableLives.size() && b,"DB_SQL_DELETE_ERROR");

        // TODO 批量级联删除对应的 评论 课件 录制视频
        return count;
    }

    /**
     * 根据多参数批量删除
     * @param paramKey   删除参数key
     * @param paramVal   删除参数val
     * @param permId
     * @param isCascade  是否级联操作
     * @param cascadeId  级联标志
     * @param userInfo
     * @param strictMode 严格模式，true：未找到要删除的记录直接报错、false：未找到要删除的记录直接返回不会报错
     * @author WX
     * @date 2021-02-23 14:34:27
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
        return null;
    }
}
