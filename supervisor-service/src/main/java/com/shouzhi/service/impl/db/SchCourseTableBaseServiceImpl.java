package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.mapper.SchCourseTableBaseMapper;
import com.shouzhi.pojo.db.*;
import com.shouzhi.pojo.vo.SchCourseTableGridVo;
import com.shouzhi.pojo.vo.SchDeviceOperateVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 学校基础课程表表业务层接口实现类
 * @author WX
 * @date 2020-12-03 14:30:01
 */
@Service("schCourseTableBaseService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchCourseTableBaseServiceImpl implements ISchCourseTableBaseService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchCourseTableBaseMapper schCourseTableBaseMapper;

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
        return schCourseTableBaseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchCourseTableBase record) throws Exception {
        return schCourseTableBaseMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchCourseTableBase record) throws Exception {
        return schCourseTableBaseMapper.insertSelective(record);
    }

    @Override
    public SchCourseTableBase selectByPrimaryKey(String id) {
        return schCourseTableBaseMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchCourseTableBase record) throws Exception {
        return schCourseTableBaseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchCourseTableBase record) throws Exception {
        return schCourseTableBaseMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-29 15:38:06
     */
    @Override
    public SchCourseTableBase selectOneByParam(Map<String, Object> map) {
        return schCourseTableBaseMapper.selectOneByParam(map);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-03 14:10:06
     */
    @Override
    public Integer selectCount() {
        return schCourseTableBaseMapper.selectCount();
    }

    /**
     * 根据参数查询列表，仅用于前台在线巡课调用，其余地方不可调用！
     * @param map
     * @author WX
     * @date 2020-12-29 17:20:19
     */
    @Override
    public List<SchCourseTableBase> foregroundListByPage(Map<String, Object> map) {
        List<SchCourseTableBase> schCourseTableBases = schCourseTableBaseMapper.foregroundListByPage(map);
        baseService.batchGenerateSignUrl(schCourseTableBases, "schDeviceFlvOverlay");
        return schCourseTableBases;
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-03 14:14:23
     */
    @Override
    public List<SchCourseTableBase> queryListByPage(Map<String, Object> map) {
        return schCourseTableBaseMapper.queryListByPage(map);
    }

    /**
     * 批量更新根据ID
     * @param map 参数列表
     * @author WX
     * @date 2021-03-12 17:59:35
     */
    @Override
    public Integer BatchUpdate(Map<String, Object> map) throws Exception {
        return schCourseTableBaseMapper.BatchUpdate(map);
    }


    /**
     * 批量更新根据ID，不同列，注意SQL的长度限制，可根据情况分批次执行，或也可以更新mysql的设置来扩展
     * @param list 数据列表
     * @author WX
     * @date 2021-03-15 17:18:09
     */
    @Override
    public Integer BatchUpdateDiffColumn(List<SchCourseTableBase> list) throws Exception {
        return schCourseTableBaseMapper.BatchUpdateDiffColumn(list);
    }


    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-03 14:19:37
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schCourseTableBaseMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-03 14:23:16
     */
    @Override
    public List<SchCourseTableBase> BatchSelect(Map<String, Object> map) {
        return schCourseTableBaseMapper.BatchSelect(map);
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-03 14:29:50
     */
    @Override
    public Integer save(SchCourseTableBase record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());
        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校基础课程表失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_BASE, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-12-03 14:34:53
     */
    @Override
    public Integer update(SchCourseTableBase record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseTableBase originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校基础课程表失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_BASE, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID删除
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-03 14:38:36
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchCourseTableBase record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_BASE, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param schCourseTableBaseIds 学校基础课程表ID，对应学校基础课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId                权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-03 14:43:08
     */
    @Override
    public Integer batchDelete(String schCourseTableBaseIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schCourseTableBaseIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchCourseTableBase> schCourseTableBases = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schCourseTableBases) && schCourseTableBases.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_COURSE_TABLE_BASE, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schCourseTableBases, null);
        Assert.isTrue(count== schCourseTableBases.size() && b,"DB_SQL_DELETE_ERROR");
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
     * @date 2020-12-03 14:47:58
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
        return null;
    }



    /*************************************** findTableGrid Start ***************************************/
    /**
     * 查询课表-表格视图数据
     * @param map
     * @author WX
     * @date 2020-12-07 10:40:43
     */
    @Override
    public SchCourseTableGridVo findTableGrid(Map<String, Object> map) {
        List<SchCourseTableBase> schCourseTableBases = this.queryListByPage(map);
        List<List<String>> courseStrForWeek = new ArrayList<>();
        // 一周七天，一天12节
        Integer days = 7, sections = 12;
        for (int i = 1; i <= days; i++) {
            // 获取某一天的课程列表，如：周一
            List<SchCourseTableBase> courseList = this.courseListForDay(schCourseTableBases, i);
            List<String> courseStrForSection = this.courseStrForSection(courseList, sections);
            courseStrForWeek.add(courseStrForSection);
        }
        List<String> hHeader = this.hHeader(days);
        List<List<Object>> vHeader = this.vHeader(sections);
        schCourseTableBases.clear();
        schCourseTableBases=null;

        // https://github.com/Hzy0913/Timetable
        // 组装表格数据格式
        /*var courseList = [
            ['语文','语文','英语','物理','语文','数学','英语','物理','物理','数学','英语','物理'],
            ['数学','语文','物理','物理','语文','语文','语文','物理','数学','语文','语文','体育'],
            ['语文','数学','英语','物理','语文','数学','英语','物理','语文','数学','英语','物理'],
            ['数学','语文','物理','物理','语文','语文','语文','英语','数学','语文','语文','体育'],
            ['语文','数学','英语','物理','语文','数学','英语','物理','语文','数学','英语','物理'],
        ];
        var timetables = [
            ['大学英语(Ⅳ)@10203','大学英语(Ⅳ)@10203','','','','','毛概@14208','毛概@14208','','','','选修'],
            ['','','信号与系统@11302','信号与系统@11302','模拟电子技术基础@16204','模拟电子技术基础@16204','','','','','',''],
            ['大学体育(Ⅳ)','大学体育(Ⅳ)','形势与政策(Ⅳ)@15208','形势与政策(Ⅳ)@15208','','','电路、信号与系统实验','电路、信号与系统实验','','','',''],
            ['','','','','电装实习@11301','电装实习@11301','','','','大学体育','大学体育',''],
            ['','','数据结构与算法分析','数据结构与算法分析','','','','','信号与系统','信号与系统','',''],
        ];
        var timetableType = [
                [{index: '1',name: '8:30'}, 1],
                [{index: '2',name: '9:30'}, 1],
                [{index: '3',name: '10:30'}, 1],
                [{index: '4',name: '11:30'}, 1],
                [{index: '5',name: '12:30'}, 1],
                [{index: '6',name: '14:30'}, 1],
                [{index: '7',name: '15:30'}, 1],
                [{index: '8',name: '16:30'}, 1],
                [{index: '9',name: '17:30'}, 1],
                [{index: '10',name: '18:30'}, 1],
                [{index: '11',name: '19:30'}, 1],
                [{index: '12',name: '20:30'}, 1]
        ];*/
        return new SchCourseTableGridVo(hHeader, vHeader, courseStrForWeek);
    }

    /**
     * 获取某一天的课程列表，如：周一
     * @param schCourseTableBases 筛选好的课表数据源
     * @param week 某一天，如：周一
     * @author WX
     * @date 2020-12-14 10:22:08
     */
    public List<SchCourseTableBase> courseListForDay(List<SchCourseTableBase> schCourseTableBases, Integer week){
        // 获取某一天的全部课，如：拿出周一的
        List<SchCourseTableBase> week1 = schCourseTableBases.stream().filter(sct -> sct.getWeek()==week).collect(Collectors.toList());
        // 将筛选出来的数据从原数据中删除，减少下次查找的时间
        schCourseTableBases.removeAll(week1);
        return week1;
    }

    /**
     * 获取拼接好的课程字符串，多少节课内
     * @param courseListForDay 某一天的课程列表，如：周一
     * @param sections 课程节数，如：12
     * @author WX
     * @date 2020-12-14 11:16:39
     */
    public List<String> courseStrForSection(List<SchCourseTableBase> courseListForDay, Integer sections){
        List<String> courseStrForSection = new ArrayList<>();
        // 获取一天内sections节的课程字符串，
        for (int i = 1; i <= sections; i++) {
            // 获取单节的课程字符串
            String s = String.join(String.valueOf(i),"/","/");
            List<SchCourseTableBase> section1 = courseListForDay.stream().filter(sct -> sct.getSectionNum().contains(s)).collect(Collectors.toList());
            AtomicReference<String> section1Str = new AtomicReference<>("");
            section1.forEach(sct -> {
                // 课程名+教师名+空间名+周数+节数+人数
                String join = String.join("♦",sct.getCourseName(),String.join("-",DatePatterns.NORM_TIME_FORMAT.format(sct.getStartTime()),DatePatterns.NORM_TIME_FORMAT.format(sct.getEndTime())),sct.getSysPersonName(),sct.getSchSpaceName(),sct.getSchClassNames(),sct.getWeeksV(),sct.getSectionNumV().concat(String.join(String.valueOf(sct.getPeopleNum()),"[","]")));
                if(!"".equals(section1Str.get())){
                    // 说明传入的课表数据还没被检索到单体维度，同一周同一人同一天同一节只会有一节课出现，比如：第12周 张三 周二 第一节
                    section1Str.set(String.join("、",section1Str.get(),join));
                }else {
                    section1Str.set(join);
                }
            });
            courseStrForSection.add(section1Str.get());
        }
        courseListForDay.clear();
        courseListForDay=null;
        return courseStrForSection;
    }


    /**
     * 垂直表头
     * 注意：目前没有课程时间表，所以在垂直表头中并没有包含时间，也不能写死，因为还分冬令时和夏令时
     * @param sections 节次数，如：12
     * @author WX
     * @date 2020-12-14 12:39:26
     */
    public List<List<Object>> vHeader(Integer sections){
        List<List<Object>> sectionNums = new ArrayList<>();
        for (int i = 1; i <= sections; i++) {
            List<Object> objects = new ArrayList<>();
            objects.add(String.valueOf(i));
            objects.add(1);
            sectionNums.add(objects);
        }
        return sectionNums;
    }

    /**
     * 水平表头
     * @param days 天数，如：7
     * @author WX
     * @date 2020-12-14 12:41:37
     */
    public List<String> hHeader(Integer days){
        String [] dayArray = {"周一","周二","周三","周四","周五","周六","周日"};
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            dayList.add(dayArray[i-1]);
        }
        return dayList;
    }
    /*************************************** findTableGrid End ***************************************/


    /**
     * 查询巡课详情
     * @param map
     * @author WX
     * @date 2020-12-29 16:03:35
     */
    @Override
    public SchCourseTableBase findPatrolCourseDetail(Map<String, Object> map) {
        SchCourseTableBase schCourseTableBase = this.selectOneByParam(map);
        // 获取当前是第几周
        String currentWeek = schSemesterService.currentWeekByCurrentSem();
        schCourseTableBase.setCurrentWeek(currentWeek);
        // 根据课表关联的空间id查询该空间下的设备列表
        map = new HashMap<>();
        map.put("schSpaceId", schCourseTableBase.getSchSpaceId());
        List<SchDevice> schDevices = schDeviceService.queryListByPage(map);
        // 查询流媒体服务器地址(这里没有关联表查询，单独在业务里查询的)
        ServerHost serverHost = new ServerHost();
        serverHost.setHostType("2");
        serverHost = serverHostService.selectOneByParam(serverHost);
        String hostStr = serverHost.getHostProtocol()+serverHost.getHostAddr()+serverHost.getHostPort();
        // 一个教室空间下的设备列表不会太多，顶多4个，
        for (SchDevice sd : schDevices) {
            if("2".equals(sd.getDeviceType())){
                schCourseTableBase.setSchDeviceVGA(new SchDeviceOperateVo(sd.getDeviceName(), hostStr+sd.getFlvGetUrl()));
                continue;
            }
            if("overlay".equals(sd.getDeviceType())){
                schCourseTableBase.setSchDeviceFlvOverlay(hostStr+sd.getFlvGetUrl());
                continue;
            }
            if("1".equals(sd.getDeviceType())){
                if(schCourseTableBase.getSchDevice1()==null){
                    schCourseTableBase.setSchDevice1(new SchDeviceOperateVo(sd.getId(), sd.getDeviceName(), hostStr+sd.getFlvGetUrl()));
                    continue;
                }
                if(schCourseTableBase.getSchDevice2()==null){
                    schCourseTableBase.setSchDevice2(new SchDeviceOperateVo(sd.getId(), sd.getDeviceName(), hostStr+sd.getFlvGetUrl()));
                    continue;
                }
            }
        }
        baseService.generateSignUrl(schCourseTableBase, schCourseTableBase.getClass(), "schDevice1.playAddr","schDevice2.playAddr","schDeviceVGA.playAddr","schDeviceFlvOverlay");
        return schCourseTableBase;
    }

    /**
     * 前端(当天+当前时间)课程树
     * @param req
     * @author WX
     * @date 2021-01-06 15:50:35
     */
    @Override
    public List<TreeNodeVo> fgCourseTree(HttpServletRequest req) {
        // 获取当前是第几周
        String currentWeek = schSemesterService.currentWeekByCurrentSem();
        Map<String, Object> map = new HashMap<>();
        // 获取当前是周几
        map.put("week", LocalDateTime.now().getDayOfWeek().getValue());
        map.put("weeksLike", StringUtils.isBlank(currentWeek)?null:String.join(currentWeek,"/","/"));
        map.put("currentCourseTime", DatePatterns.NORM_TIME_FORMAT.format(new Date()));
        // 查询所有正在上课的课表
        List<SchCourseTableBase> schCourseTableBases = this.queryListByPage(map);
        // 与课程关联的所有空间叶子节点id
        List<String> schSpaceIdsLeaf = schCourseTableBases.stream().map(SchCourseTableBase::getSchSpaceId).collect(Collectors.toList());
        map.clear();
        map.put("list",schSpaceIdsLeaf);
        map.put("idIn","1");
        // 与课程关联的所有空间叶子节点对象
        List<SchSpace> schSpacesLeaf = schSpaceService.BatchSelect(map);

        // 与课程关联的所有空间父节点id
        List<String> schParentIds = schSpacesLeaf.stream().map(s -> Arrays.asList(s.getParentIds().split("/")))
                .flatMap(Collection::stream)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        map.clear();
        map.put("list",schParentIds);
        map.put("idIn","1");
        // 与课程关联的所有空间父节点对象
        List<SchSpace> schSpacesParent = schSpaceService.BatchSelect(map);
        Map<String, SchCourseTableBase> schSpaceIdMap = schCourseTableBases.stream().collect(Collectors.toMap(SchCourseTableBase::getSchSpaceId, sctb -> sctb, (v1, v2) -> v1));
        // 合并所有空间节点对象
        List<TreeNodeVo> treeNodeVos = Stream.of(schSpacesLeaf, schSpacesParent).flatMap(Collection::stream)
                .map(s -> {
                    JSONObject jsonObject = new JSONObject();
                    String label = "";
                    SchCourseTableBase sctb = schSpaceIdMap.get(s.getId());
                    if (sctb != null) {
                        label = s.getSpaceName()+"("+sctb.getSysPersonName()+","+sctb.getCourseName()+")";
                        jsonObject.put("schCourseTableBaseId", sctb.getId());
                    } else {
                        label = s.getSpaceName();
                    }
                    return new TreeNodeVo(s.getId(), s.getId(), s.getParentId(), label, jsonObject);
                }).collect(Collectors.toList());
        map=null;
        schCourseTableBases=null;
        schSpaceIdsLeaf=null;
        schSpacesLeaf=null;
        schParentIds=null;
        schSpacesParent=null;
        schSpaceIdMap.clear();
        return TreeNodeVo.buildTree(treeNodeVos, "0");
    }
}
