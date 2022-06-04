package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.constants.DatePatterns;
import com.shouzhi.basic.utils.WeeksUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchSemester;
import com.shouzhi.pojo.vo.DateListOfWeeksVo;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchSemesterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校学期接口
 * @author WX
 * @date 2020-12-04 11:30:28
 */
@RestController
@RequestMapping("/api/v1/schSemester")
public class SchSemesterController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchSemesterService schSemesterService;

    /**
     * 查询学校学期列表
     * @apiNote 查询学校学期列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @author WX
     * @date 2020-12-04 13:40:21
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchSemester>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                          // @RequestParam(value="className",required=false) String className,
                                                          HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchSemester>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        // map.put("classNameLike", className);
        PageHelper.startPage(pageNum,pageSize);
        List<SchSemester> schSemesters = schSemesterService.queryListByPage(map);
        PageInfo<SchSemester> pageInfo = new PageInfo<>(schSemesters);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 新增学校学期
     * @apiNote 新增学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSemester
     * @author WX
     * @date 2020-12-04 13:45:53
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> save(@PathVariable("permId") String permId, @RequestBody SchSemester schSemester,
                                     HttpServletRequest req) {
        logger.info("url={},schSemester={}", req.getServletPath(),JSON.toJSONString(schSemester));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSemesterService.save(schSemester, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 修改学校学期
     * @apiNote 修改学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSemester
     * @author WX
     * @date 2020-12-04 13:49:36
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> update(@PathVariable("permId") String permId, @RequestBody SchSemester schSemester,
                                       HttpServletRequest req) {
        logger.info("url={},schSemester={}", req.getServletPath(),JSON.toJSONString(schSemester));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSemesterService.update(schSemester, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 设置为当前最新学校学期
     * @apiNote 设置为当前最新学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2021-02-22 15:05:09
     */
    @PutMapping("/setCurrent/{permId}/{rowId}")
    public CommonResult<String> setCurrentSem(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                       HttpServletRequest req) {
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schSemesterService.setCurrentSem(rowId, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 删除学校学期
     * @apiNote 删除学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-12-04 13:53:09
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                      HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schSemesterService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 批量删除学校学期
     * @apiNote 批量删除学校学期
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSemesterIds 学校学期ID，对应学校学期信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-04 13:58:50
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId, @RequestParam("schSemesterIds") String schSemesterIds,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSemesterService.batchDelete(schSemesterIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 查询某个学期周数列表，下拉列表
     * @apiNote 查询某个学期周数列表，用于下拉列表；isCurrentSem必填，其余参数选填。
     *          isCurrentSem=1查询当前最新学期的周数列表，其余参数可不填。isCurrentSem=0时需携带其它参数
     * @param isCurrentSem 是否当前学期，注意同一学年只允许存在一个当前学期；0：否，1：是
     * @param schoolYearStart 开始学年，如：2012
     * @param schoolYearEnd 结束学年，如：2013
     * @param semNum 学期编号，1：第一学期，2：第二学期
     * @author WX
     * @date 2020-12-04 17:34:06
     */
    @PostMapping("/weekNums")
    public CommonResult<List<Integer>> findWeekNums(@RequestParam("isCurrentSem") String isCurrentSem,
                                                    @RequestParam(value="schoolYearStart",required=false) String schoolYearStart,
                                                    @RequestParam(value="schoolYearEnd",required=false) String schoolYearEnd,
                                                    @RequestParam(value="semNum",required=false) String semNum, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<Integer>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(isCurrentSem)&&"1".equals(isCurrentSem)){
            map.put("isCurrentSem", isCurrentSem);
        }else {
            map.put("schoolYearStart", schoolYearStart);
            map.put("schoolYearEnd", schoolYearEnd);
            map.put("semNum", semNum);
        }
        List<Integer> weekNums = schSemesterService.findWeekNums(map, req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(weekNums);
    }

    /**
     * 查询学期总周数
     * @apiNote 传入‘学期开始日期’和‘学期结束日期’，查询计算学期总周数。一般用于反填总周数；
     * @param semDateStart 学期开始日期，格式yyyy-MM-dd，如：2012-02-28
     * @param semDateEnd 学期结束日期，如：2012-07-01
     * @author WX
     * @date 2020-12-04 17:34:06
     */
    @PostMapping("/calculateTotalWeeks")
    public CommonResult<Integer> calculateTotalWeeks(@RequestParam("semDateStart") String semDateStart,
                                                     @RequestParam("semDateEnd") String semDateEnd, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<Integer> result = new CommonResult<>();
        Integer totalWeeks = WeeksUtil.totalWeeks(semDateStart, semDateEnd);
        return result.setStatus(1).setMsg("查询成功").setResultBody(totalWeeks);
    }


    /**
     * 查询某个学期的当前周
     * @apiNote 查询目前时间位于某个学期的第几周，即：当前周；isCurrentSem必填，其余参数选填。
     *          isCurrentSem=1查询当前最新学期的当前周，其余参数可不填。isCurrentSem=0时需携带其它参数
     *          返回示例参考 "resultBody":{"currentWeek":19}
     * @param isCurrentSem 是否当前学期，注意同一学年只允许存在一个当前学期；0：否，1：是
     * @param schoolYearStart 开始学年，如：2012
     * @param schoolYearEnd 结束学年，如：2013
     * @param semNum 学期编号，1：第一学期，2：第二学期
     * @author WX
     * @date 2021-01-08 11:19:23
     */
    @PostMapping("/currentWeekBySem")
    public CommonResult<Map<String, Integer>> currentWeekBySem(@RequestParam("isCurrentSem") String isCurrentSem,
                                                 @RequestParam(value="schoolYearStart",required=false) String schoolYearStart,
                                                 @RequestParam(value="schoolYearEnd",required=false) String schoolYearEnd,
                                                 @RequestParam(value="semNum",required=false) String semNum, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<Map<String, Integer>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(isCurrentSem)&&"1".equals(isCurrentSem)){
            map.put("isCurrentSem", isCurrentSem);
        }else {
            map.put("schoolYearStart", schoolYearStart);
            map.put("schoolYearEnd", schoolYearEnd);
            map.put("semNum", semNum);
        }
        String currentWeek = schSemesterService.currentWeekBySem(map);
        HashMap<String, Integer> retMap = new HashMap<>();
        retMap.put("currentWeek", StringUtils.isNotBlank(currentWeek)?Integer.valueOf(currentWeek):null);
        return result.setStatus(1).setMsg("查询成功").setResultBody(retMap);
    }


    /**
     * 查询某个周的日期列表
     * @apiNote 查询某个周的日期列表，默认返回当前最新学期周数范围内某个周的日期列表
     * @param weeks 周数
     * @author WX
     * @date 2021-03-22 18:33:26
     */
    @PostMapping("/dateListOfWeeks")
    public CommonResult<DateListOfWeeksVo> dateListOfWeeks(@RequestParam("weeks") String weeks, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<DateListOfWeeksVo> result = new CommonResult<>();
        DateListOfWeeksVo dateListOfWeeksVo = schSemesterService.dateListOfWeeks(weeks);
        return result.setStatus(1).setMsg("查询成功").setResultBody(dateListOfWeeksVo);
    }
}
