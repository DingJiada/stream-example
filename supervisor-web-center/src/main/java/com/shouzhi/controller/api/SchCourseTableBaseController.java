package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.SchCourseTableBaseLiveSourceVo;
import com.shouzhi.pojo.vo.SchCourseTableGridVo;
import com.shouzhi.pojo.vo.DetectWeekResultVo;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
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
 * 学校基础课程表接口
 * @description (课表数据目前仅保存当前新学期的数据,未做学期表关联,数据来源自同步第三方中间库)
 * @author WX
 * @date 2020-12-03 14:44:26
 */
@RestController
@RequestMapping("/api/v1/schCourseTableBase")
public class SchCourseTableBaseController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableBaseService schCourseTableBaseService;

    /**
     * 查询学校基础课程表列表
     * @apiNote 查询学校基础课程表列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param courseName 课程名称
     * @param week 周几，星期几，如：1、2、7等
     * @param weeks 周数，如：1、2、5、18等
     * @param sectionNum 节次数，如：1、2、8、12等
     * @param sysUserId 系统用户ID
     * @param schSpaceId 学校空间信息id
     * @param sysDepartmentId 部门组织id
     * @author WX
     * @date 2020-12-03 14:53:13
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableBase>>
        findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                 @RequestParam(value="courseName",required=false) String courseName, @RequestParam(value="week",required=false) String week,
                 @RequestParam(value="weeks",required=false) String weeks, @RequestParam(value="sectionNum",required=false) String sectionNum,
                 @RequestParam(value="sysUserId",required=false) String sysUserId, @RequestParam(value="schSpaceId",required=false) String schSpaceId,
                 @RequestParam(value="sysDepartmentId",required=false) String sysDepartmentId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableBase>> result = new CommonResult<>();
        Map<String, Object> map = this.buildQMap(courseName, week, weeks, sectionNum, sysUserId, schSpaceId, sysDepartmentId);
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseTableBase> schCourseTableBases = schCourseTableBaseService.queryListByPage(map);
        PageInfo<SchCourseTableBase> pageInfo = new PageInfo<>(schCourseTableBases);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    private Map<String, Object> buildQMap(String courseName, String week, String weeks, String sectionNum, String sysUserId,
                                          String schSpaceId, String sysDepartmentId){
        Map<String, Object> map = new HashMap<>();
        map.put("courseNameLike", courseName);
        map.put("week", week);
        map.put("weeksLike", StringUtils.isBlank(weeks)?null:String.join(weeks,"/","/"));
        map.put("sectionNumLike", StringUtils.isBlank(sectionNum)?null:String.join(sectionNum,"/","/"));
        map.put("sysUserId", sysUserId);
        map.put("schSpaceId", schSpaceId);
        map.put("sysDepartmentId", sysDepartmentId);
        return map;
    }

    /**
     * 查询学校基础课程表-网格方块版
     * @apiNote 查询学校基础课程表-表格方块版
     * @param courseName 课程名称
     * @param week 周几，星期几，如：1、2、7等
     * @param weeks 周数，如：1、2、5、18等
     * @param sectionNum 节次数，如：1、2、8、12等
     * @param sysUserId 系统用户ID
     * @param schSpaceId 学校空间信息id
     * @param sysDepartmentId 部门组织id
     * @author WX
     * @date 2020-12-07 10:37:52
     */
    @PostMapping("/tableGrid")
    public CommonResult<SchCourseTableGridVo>
    findTableGrid(@RequestParam(value="courseName",required=false) String courseName, @RequestParam(value="week",required=false) String week,
             @RequestParam(value="weeks",required=false) String weeks, @RequestParam(value="sectionNum",required=false) String sectionNum,
             @RequestParam(value="sysUserId",required=false) String sysUserId, @RequestParam(value="schSpaceId",required=false) String schSpaceId,
             @RequestParam(value="sysDepartmentId",required=false) String sysDepartmentId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<SchCourseTableGridVo> result = new CommonResult<>();
        Map<String, Object> map = this.buildQMap(courseName, week, weeks, sectionNum, sysUserId, schSpaceId, sysDepartmentId);
        SchCourseTableGridVo tableGrid = schCourseTableBaseService.findTableGrid(map);
        return result.setStatus(1).setMsg("查询成功").setResultBody(tableGrid);
    }


    /**
     * 查询学校基础课程表-课表直播源
     * @apiNote 询学校基础课程表-课表直播源，直播课程表的记录源于此接口。
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param courseName 课程名称
     * @param week 周几，星期几，如：1、2、7等
     * @param weeks 周数，如：1、2、5、18等
     * @param sectionNum 节次数，如：1、2、8、12等
     * @param sysUserId 系统用户ID
     * @param schSpaceId 学校空间信息id
     * @param sysDepartmentId 部门组织id
     * @author WX
     * @date 2021-03-16 09:33:16
     */
    @PostMapping("/courseLiveSource/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableBaseLiveSourceVo>>
    courseLiveSource(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value="courseName",required=false) String courseName, @RequestParam(value="week",required=false) String week,
                  @RequestParam(value="weeks",required=false) String weeks, @RequestParam(value="sectionNum",required=false) String sectionNum,
                  @RequestParam(value="sysUserId",required=false) String sysUserId, @RequestParam(value="schSpaceId",required=false) String schSpaceId,
                  @RequestParam(value="sysDepartmentId",required=false) String sysDepartmentId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableBaseLiveSourceVo>> result = new CommonResult<>();
        Map<String, Object> map = this.buildQMap(courseName, week, weeks, sectionNum, sysUserId, schSpaceId, sysDepartmentId);
        map.put("isJoinedLiveAll", "0");
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseTableBase> schCourseTableBases = schCourseTableBaseService.queryListByPage(map);
        List<SchCourseTableBaseLiveSourceVo> r = schCourseTableBaseService.courseLiveSource(schCourseTableBases);
        PageInfo<SchCourseTableBaseLiveSourceVo> pageInfo = new PageInfo<>(r);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }



    // 前端界面没有增删改操作，所以暂时注释掉
    /**
     * 新增学校基础课程表
     * @apiNote 新增学校基础课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseTableBase
     * @author WX
     * @date 2020-12-03 14:58:01
     */
    /*@PostMapping("/save/{permId}")
    public CommonResult<String> save(@PathVariable("permId") String permId, @RequestBody SchCourseTableBase schCourseTableBase,
                                     HttpServletRequest req) {
        logger.info("url={},schCourseTableBase={}", req.getServletPath(),JSON.toJSONString(schCourseTableBase));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableBaseService.save(schCourseTableBase, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }*/


    // 前端界面没有增删改操作，所以暂时注释掉
    /**
     * 修改学校基础课程表
     * @apiNote 修改学校基础课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseTableBase
     * @author WX
     * @date 2020-12-03 15:05:36
     */
    /*@PutMapping("/update/{permId}")
    public CommonResult<String> update(@PathVariable("permId") String permId, @RequestBody SchCourseTableBase schCourseTableBase,
                                       HttpServletRequest req) {
        logger.info("url={},schCourseTableBase={}", req.getServletPath(),JSON.toJSONString(schCourseTableBase));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableBaseService.update(schCourseTableBase, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }*/

    // 前端界面没有增删改操作，所以暂时注释掉
    /**
     * 删除学校基础课程表
     * @apiNote 删除学校基础课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-12-03 15:09:57
     */
    /*@DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                      HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableBaseService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }*/


    // 前端界面没有增删改操作，所以暂时注释掉
    /**
     * 批量删除学校基础课程表
     * @apiNote 批量删除学校基础课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseTableBaseIds 学校基础课程表ID，对应学校基础课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-03 15:14:25
     */
    /*@DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId, @RequestParam("schCourseTableBaseIds") String schCourseTableBaseIds,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableBaseService.batchDelete(schCourseTableBaseIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }*/

    /**
     * 后台管理-直播管理-直播课表-制定直播计划-按教务课表自动生成计划-检测接口
     * @apiNote 后台管理-直播管理-直播课表-制定直播计划-按教务课表自动生成计划-检测接口
     * 根据week查询的结果然后根据基础课表中的 is_join_live 和 join_live_all 判断， is_join_live 只要有不为0的，返-1， join_live_all 只要有不为0的，返-2，都为0，返1
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param weeks 周数
     * @author Dingjd
     * @date 2021/3/18 15:38
     **/
    @PostMapping("/detectWeek/{permId}/{weeks}")
    public CommonResult<String> detectWeek(@PathVariable("permId") String permId, @PathVariable("weeks") String weeks, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            DetectWeekResultVo detectWeekResult = schCourseTableBaseService.detectWeek(permId, weeks, false, req);

            result.setStatus(1).setMsg("检测成功").setResultBody(detectWeekResult.getResult().toString());
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;

    }
}
