package com.shouzhi.service.impl.db;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.basic.utils.WeeksUtil;
import com.shouzhi.mapper.SchSemesterMapper;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SchSemester;
import com.shouzhi.pojo.vo.DateListOfWeeksVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.constants.DBConst;
import com.shouzhi.service.interf.db.ILogOperService;
import com.shouzhi.service.interf.db.ISchSemesterService;
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
 * 学校学期表业务层接口实现类
 * @author WX
 * @date 2020-12-04 11:23:01
 */
@Service("schSemesterService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class SchSemesterServiceImpl implements ISchSemesterService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchSemesterMapper schSemesterMapper;

    @Autowired
    private ILogOperService logOperService;

    @Autowired
    BaseService baseService;

    @Override
    public Integer deleteByPrimaryKey(String id) throws Exception {
        return schSemesterMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(SchSemester record) throws Exception {
        return schSemesterMapper.insert(record);
    }

    @Override
    public Integer insertSelective(SchSemester record) throws Exception {
        return schSemesterMapper.insertSelective(record);
    }

    @Override
    public SchSemester selectByPrimaryKey(String id) {
        return schSemesterMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SchSemester record) throws Exception {
        return schSemesterMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer updateByPrimaryKey(SchSemester record) throws Exception {
        return schSemesterMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据参数查询
     * @param map
     * @author WX
     * @date 2020-12-07 09:45:06
     */
    @Override
    public SchSemester selectOneByParam(Map<String, Object> map) {
        return schSemesterMapper.selectOneByParam(map);
    }

    /**
     * 查询总数
     * @author WX
     * @date 2020-12-04 11:19:06
     */
    @Override
    public Integer selectCount() {
        return schSemesterMapper.selectCount();
    }

    /**
     * 根据参数查询列表
     * @param map
     * @author WX
     * @date 2020-12-04 11:23:17
     */
    @Override
    public List<SchSemester> queryListByPage(Map<String, Object> map) {
        return schSemesterMapper.queryListByPage(map);
    }

    /**
     * 批量删除
     * @param map
     * @author WX
     * @date 2020-12-04 11:28:50
     */
    @Override
    public Integer batchDelete(Map<String, Object> map) throws Exception {
        return schSemesterMapper.batchDelete(map);
    }

    /**
     * 批量查询
     * @param map
     * @author WX
     * @date 2020-12-04 11:32:09
     */
    @Override
    public List<SchSemester> BatchSelect(Map<String, Object> map) {
        return schSemesterMapper.BatchSelect(map);
    }

    /**
     * 新增
     * @param record
     * @param permId
     * @author WX
     * @date 2020-12-04 11:36:10
     */
    @Override
    public Integer save(SchSemester record, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        Integer totalWeeks = WeeksUtil.totalWeeks(DatePatterns.NORM_DATE_FORMAT.format(record.getSemDateStart()), DatePatterns.NORM_DATE_FORMAT.format(record.getSemDateEnd()));
        Assert.isTrue(totalWeeks==record.getTotalWeeks(),"SCH_SEMESTER_TOTAL_WEEKS_NOT_EQUAL_ERROR");
        record.setId(UuidUtil.get32UUID());
        record.setCreateId(userInfo.getId());
        record.setCreateBy(userInfo.getUserName());

        // 将现在'是当前学期'的记录改为'否'
        if("1".equals(record.getIsCurrentSem())){
            this.unSetCurrentSem(permId, req);
        }

        Integer count = this.insertSelective(record);
        Assert.isTrue(count==1,"插入学校学期失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SEMESTER, DBConst.OPER_TYPE_INSERT,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), null, JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID更新
     * @param record
     * @param permId   权限ID或菜单ID(仅限于最后级别的菜单)
     * @param operType
     * @author WX
     * @date 2020-12-04 11:40:53
     */
    @Override
    public Integer update(SchSemester record, String permId, HttpServletRequest req, String... operType) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchSemester originalRecord = this.selectByPrimaryKey(record.getId());
        Assert.notNull(originalRecord,"DB_SQL_ID_INVALID_ERROR！");
        // 校验总周数
        if(record.getSemDateStart()!=null||record.getSemDateEnd()!=null){
            Integer totalWeeks = WeeksUtil.totalWeeks(DatePatterns.NORM_DATE_FORMAT.format(record.getSemDateStart()), DatePatterns.NORM_DATE_FORMAT.format(record.getSemDateEnd()));
            Assert.isTrue(totalWeeks==record.getTotalWeeks(),"SCH_SEMESTER_TOTAL_WEEKS_NOT_EQUAL_ERROR");
        }
        Integer count = this.updateByPrimaryKeySelective(record);
        Assert.isTrue(count==1,"更新学校学期失败！");
        // 插入操作日志
        logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SEMESTER, operType.length>0?operType[0]: DBConst.OPER_TYPE_UPDATE,
                permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(originalRecord), JSON.toJSONString(record));
        return count;
    }

    /**
     * 根据ID 设置为当前学校学期
     * @param rowId  被操作记录行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-22 15:05:09
     */
    @Override
    public Integer setCurrentSem(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 检查rowId对应的记录是否已经'是当前学期'
        SchSemester checkSem = this.selectByPrimaryKey(rowId);
        Assert.isTrue("0".equals(checkSem.getIsCurrentSem()),"SCH_SEMESTER_ALREADY_IS_CURRENT_SEM_ERROR");
        // 将现在'是当前学期'的记录改为'否'
        this.unSetCurrentSem(permId, req);
        // 将新传入的记录ID'是否当前学期'的改为'是'
        SchSemester schSemester = new SchSemester();
        schSemester.setId(rowId);
        schSemester.setIsCurrentSem("1");
        this.update(schSemester, permId, req);
        return 1;
    }

    /**
     * 根据ID 取消设置为当前学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2021-02-22 17:43:19
     */
    private void unSetCurrentSem(String permId, HttpServletRequest req) throws Exception {
        // 将现在'是当前学期'的记录改为'否'
        HashMap<String, Object> map = new HashMap<>();
        map.put("isCurrentSem", "1");
        SchSemester originalCurrentSem = this.selectOneByParam(map);
        if(originalCurrentSem!=null){
            SchSemester schSemester = new SchSemester();
            schSemester.setId(originalCurrentSem.getId());
            schSemester.setIsCurrentSem("0");
            this.update(schSemester, permId, req);
        }
    }


    /**
     * 根据ID删除
     * @param rowId  数据行ID
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-04 11:43:31
     */
    @Override
    public Integer delete(String rowId, String permId, HttpServletRequest req) throws Exception {
        // 1.删除
        BasicAuth userInfo = baseService.getUserInfo(req);
        SchSemester record = this.selectByPrimaryKey(rowId);
        Assert.notNull(record,"DB_SQL_ID_INVALID_ERROR");
        Integer count = this.deleteByPrimaryKey(rowId);
        // 插入操作日志
        boolean b = logOperService.insertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SEMESTER, DBConst.OPER_TYPE_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, record.getId(), JSON.toJSONString(record), null);
        Assert.isTrue(count==1 && b,"DB_SQL_DELETE_ERROR");
        return count;
    }

    /**
     * 根据IDs批量删除
     * @param schSemesterIds 学校学期ID，对应学校学期信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param permId         权限ID或菜单ID(仅限于最后级别的菜单)
     * @author WX
     * @date 2020-12-04 11:46:29
     */
    @Override
    public Integer batchDelete(String schSemesterIds, String permId, HttpServletRequest req) throws Exception {
        BasicAuth userInfo = baseService.getUserInfo(req);
        // 查询要删除的数据是否存在
        List<String> list = Arrays.asList(schSemesterIds.split(","));
        Assert.isTrue(CollectionUtils.isNotEmpty(list),"COMMON_INVALID_ARG_ERROR");
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("idIn","1");
        List<SchSemester> schSemesters = this.BatchSelect(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(schSemesters) && schSemesters.size()==list.size(),"DB_SQL_DELETE_ERROR");
        // 1.批量删除
        Integer count = this.batchDelete(map);
        // 批量插入操作日志
        boolean b = logOperService.batchInsertLogOperAndDetail(DBConst.TABLE_NAME_WR_SCH_SEMESTER, DBConst.OPER_TYPE_BATCH_DELETE, permId, DBConst.NO_CASCADE, null, userInfo, DBConst.TABLE_UNIFIED_ID, schSemesters, null);
        Assert.isTrue(count== schSemesters.size() && b,"DB_SQL_DELETE_ERROR");
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
     * @date 2020-12-04 11:49:16
     */
    @Override
    public Integer batchDeleteByMultiParam(String paramKey, Object paramVal, String permId, String isCascade, String cascadeId, BasicAuth userInfo, boolean strictMode) throws Exception {
        return null;
    }

    /**
     * 查询当前最新学期
     * @author WX
     * @date 2021-03-22 19:20:07
     */
    @Override
    public SchSemester selectCurrentSem() {
        Map<String, Object> map = new HashMap<>();
        map.put("isCurrentSem", "1");
        return this.selectOneByParam(map);
    }

    /**
     * 查询某个学期周数列表
     * @param map
     * @author WX
     * @date 2020-12-04 17:50:36
     */
    @Override
    public List<Integer> findWeekNums(Map<String, Object> map, HttpServletRequest req) {
        SchSemester schSemester = this.selectOneByParam(map);
        ArrayList<Integer> weekNums = new ArrayList<>();
        if(schSemester==null) return weekNums;
        for (int i = 1; i <= schSemester.getTotalWeeks(); i++) {
            weekNums.add(i);
        }
        return weekNums;
    }

    /**
     * 根据某个学期获取当前为第几周，即当前周
     * @param map
     * @author WX
     * @date 2021-01-06 15:56:07
     */
    @Override
    public String currentWeekBySem(Map<String, Object> map) {
        // 根据map参数获取到某条学期记录，根据这条学期记录的开始时间、结束时间、当前时间算出当前周
        SchSemester schSemester = this.selectOneByParam(map);
        return WeeksUtil.currentWeek(DatePatterns.NORM_DATE_FORMAT.format(schSemester.getSemDateStart()),
                DatePatterns.NORM_DATE_FORMAT.format(schSemester.getSemDateEnd()), DatePatterns.NORM_DATE_FORMAT.format(new Date()));
    }

    /**
     * 根据当前最新学期获取当前为第几周，即当前周
     * @author WX
     * @date 2021-01-06 16:05:18
     */
    @Override
    public String currentWeekByCurrentSem() {
        Map<String, Object> map = new HashMap<>();
        map.put("isCurrentSem", "1");
        return this.currentWeekBySem(map);
    }

    /**
     * 根据当前最新学期的开始时间和结束时间获取周数天数列表
     * @author WX
     * @date 2021-03-18 15:20:09
     */
    @Override
    public Map<String, Object> weeksDaysListByCurrentSem() {
        // 获取当前最新学期的开始时间和结束时间，并根据开始时间和结束时间获取周数天数列表
        SchSemester ss = this.selectCurrentSem();
        return WeeksUtil.weeksDaysList(DatePatterns.NORM_DATE_FORMAT.format(ss.getSemDateStart()), DatePatterns.NORM_DATE_FORMAT.format(ss.getSemDateEnd()));
    }

    /**
     * 查询某个周的日期列表，默认返回当前最新学期周数范围内某个周的日期列表
     * @param weeks
     * @author WX
     * @date 2021-03-22 19:14:16
     */
    @Override
    public DateListOfWeeksVo dateListOfWeeks(String weeks) {
        // 获取当前最新学期的开始时间和结束时间，并根据开始时间和结束时间获取某个周的日期列表
        SchSemester ss = this.selectCurrentSem();
        List<String> dateStrList = WeeksUtil.dateStrListOfWeeks(DatePatterns.NORM_DATE_FORMAT.format(ss.getSemDateStart()), DatePatterns.NORM_DATE_FORMAT.format(ss.getSemDateEnd()), weeks);
        Map<Integer, String> weekDate = WeeksUtil.weekOfDateStrList(dateStrList);
        return new DateListOfWeeksVo(dateStrList, weekDate);
    }
}
