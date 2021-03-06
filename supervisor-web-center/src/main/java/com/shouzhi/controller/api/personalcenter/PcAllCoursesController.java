package com.shouzhi.controller.api.personalcenter;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.PcAllCoursesVO;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心-我的课程-全部课程
 * @author WX
 * @date 2021-03-19 13:54:40
 */
@RestController
@RequestMapping("/api/v1/pc/allCourses")
public class PcAllCoursesController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableBaseService schCourseTableBaseService;


    /**
     * 查询个人中心-我的课程-全部课程
     * @apiNote 查询个人中心-我的课程-全部课程
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param week 周
     * @param weeks 周数
     * @author Dingjd
     * @date 2021/3/23 10:33
     **/
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<PcAllCoursesVO>>
    findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
             @RequestParam("week") String week, @RequestParam("weeks") String weeks, HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<PcAllCoursesVO>> result = new CommonResult<>();
        PageHelper.startPage(pageNum,pageSize);
        List<PcAllCoursesVO> pcAllCoursesVOList = schCourseTableBaseService.querySelfAllCourse(week, weeks, req);
        PageInfo<PcAllCoursesVO> pageInfo = new PageInfo<>(pcAllCoursesVOList);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }
}
