package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseTableBase;
import com.shouzhi.pojo.db.SchCourseTableLive;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
import com.shouzhi.service.interf.db.ISchCourseTableLiveService;
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
 * 学校直播课程表接口
 * @description (直播课表数据来源学校课程表基础表，基础表内一条数据对应本表内多条数据，如：基础表id=1的1-6周有课，则本表最多会产生6条对应数据)
 * @author WX
 * @date 2021-02-23 14:49:51
 */
@RestController
@RequestMapping("/api/v1/schCourseTableLive")
public class SchCourseTableLiveController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableLiveService schCourseTableLiveService;

    /**
     * 查询学校直播课程表列表
     * @apiNote 查询学校直播课程表列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param weeks 周数，如：1、2、5、18等
     * @param sectionNum 节次数，如：1、2、8、12等
     * @param week 周几，星期几，如：1、2、7等
     * @param courseOrPersonName 课程名称或姓名(人名)
     * @param schSpaceParentIdsFuzzy 学校空间信息父id
     * @author WX
     * @date 2021-02-23 15:34:08
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableLive>>
    findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
             @RequestParam(value="weeks",required=false) String weeks,
             @RequestParam(value="sectionNum",required=false) String sectionNum,
             @RequestParam(value="week",required=false) String week,
             @RequestParam(value="courseOrPersonName",required=false) String courseOrPersonName,
             @RequestParam(value="schSpaceParentIdsFuzzy",required=false) String schSpaceParentIdsFuzzy, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableLive>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("weeks", weeks);
        map.put("sectionNumLike", StringUtils.isBlank(sectionNum)?null:String.join(sectionNum,"/","/"));
        map.put("week", week);
        map.put("courseOrPersonNameLike", courseOrPersonName);
        map.put("schSpaceParentIdsFuzzy", StringUtils.isBlank(schSpaceParentIdsFuzzy)?null:schSpaceParentIdsFuzzy);
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseTableLive> schCourseTableLives = schCourseTableLiveService.queryListByPage(map);
        PageInfo<SchCourseTableLive> pageInfo = new PageInfo<>(schCourseTableLives);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 删除学校直播课程表
     * @apiNote 删除学校直播课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2021-02-23 16:30:19
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                      HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableLiveService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 批量删除学校直播课程表
     * @apiNote 批量删除学校直播课程表
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseTableLiveIds 学校直播课程表ID，对应学校直播课程表信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2021-02-23 16:41:36
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId, @RequestParam("schCourseTableLiveIds") String schCourseTableLiveIds,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseTableLiveService.batchDelete(schCourseTableLiveIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
