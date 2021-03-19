package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.basic.utils.WeeksUtil;
import com.shouzhi.mapper.SchCourseTableLiveMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.db.SchCourseTableLive;
import com.shouzhi.pojo.db.SchSemester;
import com.shouzhi.pojo.dto.SchCourseTableLiveDto;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.impl.other.DetectWeekResult;
import com.shouzhi.service.interf.db.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ISchCourseTableBaseService schCourseTableBaseService;

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
     * 根据参数查询列表  无连接表 NoJoinTable
     * @param map
     * @author WX
     * @date 2021-03-18 09:53:19
     **/
    @Override
    public List<SchCourseTableLive> queryListByPageNJT(Map<String, Object> map) {
        return schCourseTableLiveMapper.queryListByPageNJT(map);
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
     * 批量插入
     * @param list
     * @author WX
     * @date 2021-03-12 17:43:36
     */
    @Override
    public Integer batchInsert(List<SchCourseTableLive> list) throws Exception {
        return schCourseTableLiveMapper.batchInsert(list);
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
     * 批量更新
     * @param map
     * @author Dingjd
     * @date 2021/3/17 15:15
     **/
    @Override
    public Integer batchUpdate(Map<String, Object> map) {
        return schCourseTableLiveMapper.batchUpdate(map);
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
     * 批量新增
     * @param list
     * @param permId
     * @author WX
     * @date 2021-03-12 17:47:39
     */
    @Override
    public Integer batchSave(List<SchCourseTableLive> list, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        //  批量新增学校直播课程表信息
        Integer count = this.batchInsert(list);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, list);
        Assert.isTrue(count==list.size() && b,"DB_SQL_INSERT_ERROR");
        return count;
    }

    /**
     * 加入(发布)自定义直播计划
     * @param records
     * @param permId
     * @author WX
     * @date 2021-03-12 10:57:26
     */
    @Override
    public Integer joinCustomLivePlan(List<SchCourseTableLiveDto> records, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 获取当前最新学期的周数天数列表
        Map<String, Object> weeksDaysList = schSemesterService.weeksDaysListByCurrentSem();

        Set<String> sctbIds = new HashSet<>();
        Map<String, List<Integer>> sctbIdWeeks = new HashMap<>();

        // 组装数据
        List<SchCourseTableLive> schCourseTableLives = records.parallelStream().map(r -> {
            // 收集基础课表ID，及该ID对应的周数列表
            sctbIds.add(r.getSchCourseTableBaseId());
            if(sctbIdWeeks.get(r.getSchCourseTableBaseId())!=null){
                sctbIdWeeks.get(r.getSchCourseTableBaseId()).add(r.getWeeks());
            }else {
                List<Integer> list = new ArrayList<>();
                list.add(r.getWeeks());
                sctbIdWeeks.put(r.getSchCourseTableBaseId(), list);
            }
            // 根据 周数、周几 推算出对应的日期，如：12周的周三对应的是XXXX年XX月XX日
            LocalDate localDate = WeeksUtil.dateOfWeek2s(weeksDaysList, String.valueOf(r.getWeeks()), String.valueOf(r.getWeek()), false);
            Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            SchCourseTableLive sctl = new SchCourseTableLive();
            sctl.setSchCourseTableBaseId(r.getSchCourseTableBaseId());
            sctl.setWeeks(r.getWeeks());
            sctl.setDateForWeeks(from);
            sctl.setIsRecord(r.getIsRecord());
            sctl.setPlanForm("1_1");
            sctl.setPlanCreator(userInfo.getSysUser().getPersonName());
            sctl.setIsCancel("0");
            sctl.setId(UuidUtil.get32UUID());
            sctl.setCreateId(userInfo.getId());
            sctl.setCreateBy(userInfo.getUserName());
            sctl.setCreateWay("0");
            sctl.setCreateTime(new Date());
            return sctl;
        }).collect(Collectors.toList());

        // 批量查询 sctbIds 对应的基础课表数据
        Map<String, Object> sctbQMap = new HashMap<>();
        sctbQMap.put("list",sctbIds);
        sctbQMap.put("idIn","1");
        List<SchCourseTableBase> originalSctbList = schCourseTableBaseService.BatchSelect(sctbQMap);
        Map<String, SchCourseTableBase> originalSctbIdWeeks = originalSctbList.parallelStream().collect(Collectors.toMap(s -> s.getId(), s -> s));

        // 组装已经加入直播课表所对应的基础课表数据，用于批量更新
        List<SchCourseTableBase> sctbList = sctbIds.parallelStream().map(s -> {
            SchCourseTableBase sctb = new SchCourseTableBase();
            sctb.setId(s);
            if(!"0".equals(originalSctbIdWeeks.get(s).getIsJoinLive())){
                String liveWeeks = Stream.of(Arrays.asList(originalSctbIdWeeks.get(s).getJoinLiveWeeks().split("/")).stream().filter(StringUtils::isNotBlank).map(Integer::valueOf), sctbIdWeeks.get(s).stream())
                        .flatMap(integerStream -> integerStream).distinct().sorted().map(String::valueOf).collect(Collectors.joining("/", "/", "/"));
                sctb.setJoinLiveWeeks(liveWeeks);
            } else {
                sctb.setJoinLiveWeeks(sctbIdWeeks.get(s).parallelStream().sorted().map(String::valueOf).collect(Collectors.joining("/", "/", "/")));
                sctb.setIsJoinLive("1");
            }
            if(originalSctbIdWeeks.get(s).getWeeks().length()==sctb.getJoinLiveWeeks().length()){
                sctb.setIsJoinedLiveAll("1");
            }
            return sctb;
        }).collect(Collectors.toList());

        // 批量插入
        Integer count = this.batchSave(schCourseTableLives, permId, req);
        // 批量更新已经加入直播课表的基础课表对应记录状态(要注意的问题是SQL语句的长度)
        schCourseTableBaseService.BatchUpdateDiffColumn(sctbList);

        weeksDaysList.clear();
        sctbIds.clear();
        sctbIdWeeks.clear();
        schCourseTableLives.clear();schCourseTableLives=null;
        sctbQMap.clear();sctbQMap=null;
        originalSctbList.clear();originalSctbList=null;
        originalSctbIdWeeks.clear();
        sctbList.clear();sctbList=null;
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

    /**
     * 批量取消（批量恢复）计划
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param ids 筛选id 多个,隔开
     * @param isCancel 批量取消或恢复 0 或 1
     * @author Dingjd
     * @date 2021/3/17 14:55
     **/
    @Override
    public Integer batchChangePlanService(String permId, String ids, String isCancel, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);

        List<String> list = Arrays.asList(ids.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");

        Map<String, Object> map = new HashMap<>();
        map.put("isCancel", isCancel);
        map.put("list", list);

        Integer count = this.batchUpdate(map);


        return count;
    }

    /**
     * 一键取消（一键恢复）计划
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param isCancel 是否取消，默认否（0：未取消，1：已取消）
     * @author Dingjd
     * @date 2021/3/17 16:40
     **/
    @Override
    public Integer oneKeyChangePlanService(String permId, String isCancel, HttpServletRequest req) throws Exception {

        Map<String, Object> map = new HashMap<>();

        List<String> list = new ArrayList<>();
        map.put("isCancel", Integer.parseInt(isCancel) == 0 ? "1" : "0");

        List<SchCourseTableLive> schCourseTableLives = this.queryListByPageNJT(map);

        schCourseTableLives.forEach(o -> list.add(o.getId()));

        map = new HashMap<>();
        map.put("list",list);
        map.put("isCancel",isCancel);

        Integer count = this.batchUpdate(map);

        return count;
    }

    /**
     * 后台管理-直播管理-直播课表-制定直播计划-按教务课表自动生成计划-发布业务
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param isRecord 是否录制，默认否（0：否，1：是）
     * @param week 周数
     * @author Dingjd
     * @date 2021/3/18 16:48
     **/
    @Override
    public Integer publishLivePlanService(String permId, String isRecord, String week, HttpServletRequest req) throws Exception {

        DetectWeekResult detectWeekResult = schCourseTableBaseService.detectWeek(permId, week, req);//先检测

        if (detectWeekResult.getResult() != 1) { //检测结果(result) == 1 才能进行新增操作
            return 0;
        }

        BasicAuth userInfo = baseService.getUserInfo(req);
        // 获取当前最新学期的周数天数列表
        Map<String, Object> weeksDaysList = schSemesterService.weeksDaysListByCurrentSem();

        List<SchCourseTableBase> schCourseTableBases = detectWeekResult.getSchCourseTableLiveList();

        List<SchCourseTableLive> schCourseTableLiveList = schCourseTableBases.stream().map(s -> {
            String id = s.getId();
            return Arrays.stream(s.getWeeks().substring(1).split("/")).map( str -> {
                LocalDate localDate = WeeksUtil.dateOfWeek2s(weeksDaysList, String.valueOf(str), String.valueOf(s.getWeek()), false);
                Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                SchCourseTableLive schCourseTableLive = new SchCourseTableLive();
                schCourseTableLive.setId(UuidUtil.get32UUID());//主键id
                schCourseTableLive.setSchCourseTableBaseId(id);//外键id
                schCourseTableLive.setWeeks(Integer.parseInt(str));//weeks(拆分后)
                schCourseTableLive.setDateForWeeks(from);//计算后的日期
                schCourseTableLive.setIsRecord(isRecord);//是否录制
                schCourseTableLive.setPlanForm("1_1");//来源：1_1(自动生成)
                schCourseTableLive.setPlanCreator(userInfo.getSysUser().getPersonName());//管理员名称
                schCourseTableLive.setIsCancel("0");//填充默认值0
                schCourseTableLive.setCreateId(userInfo.getId());
                schCourseTableLive.setCreateBy(userInfo.getUserName());
                schCourseTableLive.setCreateWay("1");
                schCourseTableLive.setCreateTime(new Date());
                return schCourseTableLive;
            }).collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());

        //批量新增直播计划
        Integer count = this.batchInsert(schCourseTableLiveList);

        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE, DBConst.OPER_TYPE_BATCH_INSERT, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, null, schCourseTableLiveList);
        Assert.isTrue(count == schCourseTableLiveList.size() && b,"DB_SQL_INSERT_ERROR");

        return count;
    }
}
