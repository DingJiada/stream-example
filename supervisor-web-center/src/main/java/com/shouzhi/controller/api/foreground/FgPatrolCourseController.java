package com.shouzhi.controller.api.foreground;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
import com.shouzhi.service.interf.db.ISchSemesterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端平台-在线巡课
 * @author WX
 * @date 2020-12-29 14:15:47
 */
@RestController
@RequestMapping("/api/v1/fg/patrolCourse")
public class FgPatrolCourseController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableBaseService schCourseTableBaseService;

    @Autowired
    private ISchSemesterService schSemesterService;

    /**
     * 查询学校正在上课的基础课程表(教室)列表
     * @apiNote 查询学校正在上课的基础课程表(教室)列表，默认获取当前最新学期+当前第几周+当前周几+当前上课时间范围前后的课程
     *          如：当前学期+当前为第19周+今天周四+09:00:09范围前后的课程，例：语文 08:00:00-09:45:00 则会被搜索到显示出来
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param courseOrPersonName 课程名称或姓名(人名)
     * @param schCourseCategoryId 学校课程类别ID
     * @param schSpaceParentIdsFuzzy 学校空间信息父id
     * @param sysDepParentIdsFuzzy 部门组织父id
     * @author WX
     * @date 2020-12-29 14:22:36
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableBase>>
    findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
             @RequestParam(value="courseOrPersonName",required=false) String courseOrPersonName,
             @RequestParam(value="schCourseCategoryId",required=false) String schCourseCategoryId,
             @RequestParam(value="schSpaceParentIdsFuzzy",required=false) String schSpaceParentIdsFuzzy,
             @RequestParam(value="sysDepParentIdsFuzzy",required=false) String sysDepParentIdsFuzzy,HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableBase>> result = new CommonResult<>();
        // 获取当前是第几周
        String currentWeek = schSemesterService.currentWeekByCurrentSem();
        // 获取当前是周几
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        Map<String, Object> map = new HashMap<>();
        map.put("courseOrPersonNameLike", courseOrPersonName);
        map.put("week", dayOfWeek);
        map.put("weeksLike", StringUtils.isBlank(currentWeek)?null:String.join(currentWeek,"/","/"));
        map.put("schCourseCategoryId", schCourseCategoryId);
        map.put("currentCourseTime", DatePatterns.NORM_TIME_FORMAT.format(new Date()));
        map.put("schSpaceParentIdsFuzzy", StringUtils.isBlank(schSpaceParentIdsFuzzy)?null:schSpaceParentIdsFuzzy);
        map.put("sysDepParentIdsFuzzy", StringUtils.isBlank(sysDepParentIdsFuzzy)?null:sysDepParentIdsFuzzy);
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseTableBase> schCourseTableBases = schCourseTableBaseService.foregroundListByPage(map);
        PageInfo<SchCourseTableBase> pageInfo = new PageInfo<>(schCourseTableBases);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 查询学校正在上课的基础课程表(教室)树
     * @apiNote 查询学校正在上课的基础课程表(教室)树，默认获取当前最新学期+当前第几周+当前周几+当前上课时间范围前后的课程
     *          如：当前学期+当前为第19周+今天周四+09:00:09范围前后的课程，例：语文 08:00:00-09:45:00 则会被搜索到显示出来
     * @author WX
     * @date 2021-01-06 15:50:35
     */
    @PostMapping("/courseTree")
    public CommonResult<List<TreeNodeVo>> fgCourseTree(HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<TreeNodeVo>> result = new CommonResult<>();
        List<TreeNodeVo> tree = schCourseTableBaseService.fgCourseTree(req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(tree);
    }


    /**
     * 查询学校正在上课的某个教室详情
     * @apiNote 查询学校正在上课的某个教室详情，需将上层[基础课程表(教室)列表]中的<b>id</b>带过来，当作此接口的必要查询参数
     * @param schCourseTableBaseId 基础课程表ID
     * @author WX
     * @date 2020-12-29 15:57:19
     */
    @PostMapping("/findDetail")
    public CommonResult<SchCourseTableBase> findDetail(@RequestParam("schCourseTableBaseId") String schCourseTableBaseId,
                                                       HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<SchCourseTableBase> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", schCourseTableBaseId);
        SchCourseTableBase patrolCourseDetail = schCourseTableBaseService.findPatrolCourseDetail(map);
        return result.setStatus(1).setMsg("查询成功").setResultBody(patrolCourseDetail);
    }

}
