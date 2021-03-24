package com.shouzhi.controller.api.personalcenter;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseTableLive;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchCourseTableLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 个人中心-我的课程-直播课程
 * @author WX
 * @date 2021-03-19 14:02:15
 */
@RestController
@RequestMapping("/api/v1/pc/liveCourses")
public class PcLiveCoursesController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableLiveService schCourseTableLiveService;

    // TODO SOMETHING

    /**
     * 查询个人中心-我的课程-直播课程
     * @apiNote 查询个人中心-我的课程-直播课程
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param week 周
     * @param weeks 周数
     * @param dataForWeeks 筛选日期
     * @author Dingjd
     * @date 2021/3/24 17:16
     **/
    @PostMapping("/findLiveCourseList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseTableLive>>
        findLiveCourseList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                           @RequestParam(value = "week", required = false) String week, @RequestParam(value = "weeks", required = false) String weeks,
                           @RequestParam(value = "dataForWeeks", required = false) String dataForWeeks, HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseTableLive>> result = new CommonResult<>();

        PageHelper.startPage(pageNum,pageSize);

        List<SchCourseTableLive> schCourseTableLiveList = schCourseTableLiveService.querySelfLiveCourse(dataForWeeks, week, weeks, req);

        PageInfo<SchCourseTableLive> pageInfo = new PageInfo<>(schCourseTableLiveList);

        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }
}
