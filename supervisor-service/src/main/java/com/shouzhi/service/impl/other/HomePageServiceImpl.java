package com.shouzhi.service.impl.other;

import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.pojo.db.*;
import com.shouzhi.pojo.echarts.BarChart;
import com.shouzhi.pojo.echarts.PieChart;
import com.shouzhi.pojo.echarts.PieChartData;
import com.shouzhi.pojo.vo.HomePageCurrentDateTimeVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.DistinctWrapper;
import com.shouzhi.service.interf.db.*;
import com.shouzhi.service.interf.other.IHomePageService;
import com.xkzhangsan.time.LunarDate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台首页业务层接口实现类
 * @author WX
 * @date 2021-01-20 16:54:50
 */
@Service("homePageService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class HomePageServiceImpl implements IHomePageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchSpaceService schSpaceService;

    @Autowired
    private ISchSemesterService schSemesterService;

    @Autowired
    BaseService baseService;

    @Autowired
    private ISysStaticParamService sysStaticParamService;

    @Autowired
    private ILogLoginService logLoginService;

    @Autowired
    private ISysRoleService sysRoleService;


    /**
     * 当前日期时间
     * @param req
     * @author WX
     * @date 2021-01-20 16:43:25
     */
    @Override
    public HomePageCurrentDateTimeVo currentDateTime(HttpServletRequest req) throws Exception {
        // 查询学校空间根节点，用于填充‘学校名称’
        HashMap<String, Object> map = new HashMap<>();
        map.put("parentId","0");
        map.put("spaceType","1");
        SchSpace schSpace = schSpaceService.selectOneByParam(map);
        String schoolName = Optional.ofNullable(schSpace).orElseGet(SchSpace::new).getSpaceName();
        // 查询当前教学周
        String currentWeek = schSemesterService.currentWeekByCurrentSem();
        // 当前日期 阳历
        String gDate = DatePatterns.CN_DATE_FORMAT.format(new Date());
        LunarDate now = LunarDate.now();
        // 当前日期 阴历
        String lDate = now.formatLongCnWithChineseHoliday();
        // 当前星期
        String weekCn = now.getWeekCn();
        // 人名
        BasicAuth userInfo = baseService.getUserInfo(req);
        String personName = userInfo.getSysUser().getPersonName();
        // 上次登陆时间
        String lastLoginTime = DatePatterns.NORM_DATETIME_FORMAT.format(userInfo.getLastLoginTime());
        // 系统平台名称 1_5_2
        SysStaticParam sysStaticParam = sysStaticParamService.selectByPrimaryKey("1_5_2");
        String sysPlatformName = Optional.ofNullable(sysStaticParam).orElseGet(SysStaticParam::new).getParamVal();
        return new HomePageCurrentDateTimeVo(schoolName, currentWeek, weekCn, gDate, lDate, personName, sysPlatformName, lastLoginTime);
    }

    /**
     * 访问人数统计模块
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @param req
     * @author WX
     * @date 2021-01-22 16:10:06
     */
    @Override
    public BarChart visitorsNum(String dateType, HttpServletRequest req) throws Exception {
        List<String> legendData = new ArrayList<>();
        List<String> xAxisData = new ArrayList<>();
        List<Integer> yAxisData = new ArrayList<>();

        // 拿到所有角色名称，转换为对应的map，key为角色名称，val为该角色对应的人数(默认为0)
        List<SysRole> sysRoles = sysRoleService.queryListByPage(new SysRole());
        Map<String, Integer> rNameMap = sysRoles.parallelStream().collect(Collectors.toMap(SysRole::getRoleName, s -> 0));

        // 获取数据库数据
        List<LogLogin> logLoginList = this.queryLogLoginList(dateType);

        // 将拿到的访问记录去重后，匹配每条访问记录所属哪个角色，并为该角色的count加一，一人可能会有多个角色
        logLoginList.parallelStream()
                .filter(l -> StringUtils.isNotBlank(l.getRoleName()))
                .map(l -> new LogLogin(l.getCreateId(), l.getCreateBy(), l.getRoleName()))
                .filter(DistinctWrapper.byKey(LogLogin::getCreateId))
                .forEach(l -> {
                    List<String> roleNames = Arrays.asList(l.getRoleName().split(","));
                    roleNames.parallelStream().forEach(r -> {
                        Integer count = rNameMap.get(r);
                        if(count!=null)
                            rNameMap.put(r, count + 1);
                    });
                });
        // 将rNameMap内的数据转换为坐标数据
        rNameMap.entrySet().parallelStream().forEach(entry -> {
            legendData.add(entry.getKey());
            xAxisData.add(entry.getKey());
            yAxisData.add(entry.getValue());
        });

        sysRoles.clear();
        sysRoles=null;
        rNameMap.clear();
        logLoginList.clear();
        logLoginList=null;
        return new BarChart(legendData,xAxisData,yAxisData);
    }

    /**
     * 访问来源-操作系统统计模块
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @param sourceType 来源类型，1：操作系统、2：浏览器
     * @param req
     * @author WX
     * @date 2021-01-25 16:59:07
     */
    @Override
    public PieChart visitorSource(String dateType, String sourceType, HttpServletRequest req) throws Exception {
        List<String> legendData = new ArrayList<>();
        List<PieChartData> pieData = new ArrayList<>();
        // 获取数据库数据
        List<LogLogin> logLoginList = this.queryLogLoginList(dateType);
        Map<String, List<LogLogin>> OsNameMap = new HashMap<>();
        switch (sourceType){
            case "1":
                OsNameMap = logLoginList.parallelStream().collect(Collectors.groupingBy(LogLogin::getOsGroupName));
                break;
            case "2":
                OsNameMap = logLoginList.parallelStream().collect(Collectors.groupingBy(LogLogin::getBrowserGroupName));
                break;
        }
        // 将OsNameMap内的数据转换为饼状图数据
        OsNameMap.entrySet().parallelStream().forEach(entry -> {
            legendData.add(entry.getKey());
            pieData.add(new PieChartData(entry.getValue().size(), entry.getKey()));
        });

        logLoginList.clear();
        logLoginList=null;
        OsNameMap.clear();
        OsNameMap=null;
        return new PieChart(legendData, pieData);
    }


    /**
     * 根据dateType获取不同日期间隔的数据
     * @param dateType
     */
    private List<LogLogin> queryLogLoginList(String dateType){
        // 今天的日期
        LocalDate today = LocalDate.now();
        List<LogLogin> logLoginList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("loginState", "1");
        switch (dateType){
            case "1":
                map.put("loginTimeLike", DatePatterns.NORM_DATE_FORMAT.format(new Date()));
                logLoginList = logLoginService.queryListByPage(map);
                break;
            case "2":
                WeekFields weekFields= WeekFields.ISO;
                // 本周的第一天
                map.put("loginTimeStart", today.with(weekFields.dayOfWeek(), 1L).toString());
                // 本周的最后一天，注意：最后一天(结束时间)需要添加 23:59:59 因为没有时分秒的话默认为00:00:00，那就永远查不到最后一天的数据
                map.put("loginTimeEnd", String.join(" ", today.with(weekFields.dayOfWeek(), 7L).toString(), "23:59:59"));
                logLoginList = logLoginService.queryListByPage(map);
                break;
            case "3":
                // 本月的第一天
                map.put("loginTimeStart", today.with(TemporalAdjusters.firstDayOfMonth()).toString());
                // 本月的最后一天，注意：最后一天(结束时间)需要添加 23:59:59 因为没有时分秒的话默认为00:00:00，那就永远查不到最后一天的数据
                map.put("loginTimeEnd", String.join(" ", today.with(TemporalAdjusters.lastDayOfMonth()).toString(), "23:59:59"));
                logLoginList = logLoginService.queryListByPage(map);
                break;
            case "4":
                // 今年的第一天
                map.put("loginTimeStart", today.with(TemporalAdjusters.firstDayOfYear()).toString());
                // 今年的最后一天，注意：最后一天(结束时间)需要添加 23:59:59 因为没有时分秒的话默认为00:00:00，那就永远查不到最后一天的数据
                map.put("loginTimeEnd", String.join(" ", today.with(TemporalAdjusters.lastDayOfYear()).toString(), "23:59:59"));
                logLoginList = logLoginService.queryListByPage(map);
                break;
        }
        return logLoginList;
    }
}
