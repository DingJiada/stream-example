package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.echarts.BarChart;
import com.shouzhi.pojo.echarts.PieChart;
import com.shouzhi.pojo.vo.HomePageCurrentDateTimeVo;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
import com.shouzhi.service.interf.db.ISchSemesterService;
import com.shouzhi.service.interf.db.IShortcutMenuService;
import com.shouzhi.service.interf.other.IHomePageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页-后台-接口
 * @author WX
 * @date 2021-01-20 16:27:32
 */
@RestController
@RequestMapping("/api/v1/homePage")
public class HomePageController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IHomePageService homePageService;

    @Autowired
    private IShortcutMenuService shortcutMenuService;

    @Autowired
    private ISchCourseTableBaseService schCourseTableBaseService;

    @Autowired
    private ISchSemesterService schSemesterService;

    /**
     * 当前日期时间欢迎语模块
     * @apiNote 当前日期时间欢迎语模块，返回当前学校名称、教学周、周几、阳历日期、阴历日期等
     * @author WX
     * @date 2021-01-20 17:02:36
     */
    @PostMapping("/currentDateTime")
    public CommonResult<HomePageCurrentDateTimeVo> currentDateTime(HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<HomePageCurrentDateTimeVo> result = new CommonResult<>();
        HomePageCurrentDateTimeVo homePageCurrentDateTimeVo = homePageService.currentDateTime(req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(homePageCurrentDateTimeVo);
    }

    /**
     * 查询用户快捷菜单设置列表模块
     * @apiNote 查询用户快捷菜单设置列表，根据系统用户ID（非账户ID，不要搞混）。用于展示用户已经设置过的快捷菜单
     * @param sysUserId 系统用户ID
     * @author WX
     * @date 2021-01-22 09:16:30
     */
    @GetMapping("/shortcutMenuSettings/{sysUserId}")
    public CommonResult<List<SysPermission>> shortcutMenuSettings(@PathVariable("sysUserId") String sysUserId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        List<SysPermission> sysPermissions = shortcutMenuService.selectPermissionsByUserId(sysUserId);
        return CommonResult.<List<SysPermission>>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sysPermissions);
    }

    /**
     * 今日课程模块
     * @apiNote 今日课程模块，默认获取当前最新学期+当前第几周+当前周几的全部课程，如：当前学期+当前为第19周+今天周四的课程
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @author WX
     * @date 2021-01-22 11:37:08
     */
    @PostMapping("/todayCourse/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableBase>> todayCourse(@PathVariable("pageNum") Integer pageNum,
                                                                    @PathVariable("pageSize") Integer pageSize, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableBase>> result = new CommonResult<>();
        // 获取当前是第几周
        String currentWeek = schSemesterService.currentWeekByCurrentSem();
        // 获取当前是周几
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        Map<String, Object> map = new HashMap<>();
        map.put("week", dayOfWeek);
        map.put("weeksLike", StringUtils.isBlank(currentWeek)?null:String.join(currentWeek,"/","/"));
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseTableBase> schCourseTableBases = schCourseTableBaseService.queryListByPage(map);
        PageInfo<SchCourseTableBase> pageInfo = new PageInfo<>(schCourseTableBases);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 访问人数统计模块
     * @apiNote 访问人数统计模块，日期间隔越小越能看出效果，反之，越看不出效果。因为统计的是人数而非次数。
     *          例：总共10人，统计到今日访问6人，可以一眼看出有几人未访问。同理，统计到本周访问8人，也可以一眼看出有几人未访问。
     *          但统计到本月或本年时，假如在本月或本年里都曾经访问过，不论访问过多少次，那就一直是10了。
     *          需默认展示‘本年’的数据，因为数据多了图表才能有效果，数据少了效果不明显。
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @author WX
     * @date 2021-01-22 15:55:16
     */
    @PostMapping("/visitorsNum")
    public CommonResult<BarChart> visitorsNum(@RequestParam("dateType") String dateType, HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<BarChart> result = new CommonResult<>();
        BarChart homePageVisitorsNum = homePageService.visitorsNum(dateType, req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(homePageVisitorsNum);
    }

    /**
     * 访问来源统计模块
     * @apiNote 访问来源统计模块，该接口可返回多种来源类型统计，具体见下方sourceType参数描述
     *          需默认展示‘本年’的数据，因为数据多了图表才能有效果，数据少了效果不明显。
     * @param dateType 日期类型，1：今日、2：本周、3：本月、4：本年
     * @param sourceType 来源类型，1：操作系统、2：浏览器
     * @author WX
     * @date 2021-01-25 16:59:07
     */
    @PostMapping("/visitorSource")
    public CommonResult<PieChart> visitorSource(@RequestParam("dateType") String dateType,
                                                @RequestParam("sourceType") String sourceType, HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PieChart> result = new CommonResult<>();
        PieChart visitorSourceOS = homePageService.visitorSource(dateType, sourceType, req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(visitorSourceOS);
    }

}
