package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.ExaminationHall;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.IExaminationHallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 云监考-后台-考试管理接口
 * @author WX
 * @date 2020-08-03 12:50:42
 */
@RestController
@RequestMapping("/api/v1/examinationHall")
public class ExaminationHallController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IExaminationHallService examinationHallService;

    /**
     * 后台管理-查询考试列表
     * @apiNote 后台管理-查询考试列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param examSubjectsName 考试科目名称
     * @author WX
     * @date 2020-08-03 12:54:16
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<ExaminationHall>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                              @RequestParam(value="examSubjectsName",required=false) String examSubjectsName, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<ExaminationHall>> result = new CommonResult<>();
        ExaminationHall examinationHall = new ExaminationHall();
        examinationHall.setExamSubjectsName(examSubjectsName);
        PageHelper.startPage(pageNum,pageSize);
        List<ExaminationHall> examinationHalls = examinationHallService.queryListByPage(examinationHall);
        PageInfo<ExaminationHall> pageInfo = new PageInfo<>(examinationHalls);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增考试信息
     * @apiNote 后台管理-新增考试信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examinationHall
     * @author WX
     * @date 2020-08-03 15:42:18
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveExaminationHall(@PathVariable("permId") String permId,
                                                    @RequestBody ExaminationHall examinationHall, HttpServletRequest req) {
        logger.info("url={},examinationHall={}", req.getServletPath(),JSON.toJSONString(examinationHall));
        CommonResult<String> result = new CommonResult<>();
        try {
            examinationHallService.saveExamAndInvigilators(examinationHall, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_INSERT_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改考试信息
     * @apiNote 后台管理-修改考试信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examinationHall
     * @author WX
     * @date 2020-08-04 10:03:16
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateExaminationHall(@PathVariable("permId") String permId,
                                                      @RequestBody ExaminationHall examinationHall, HttpServletRequest req) {
        logger.info("url={},examinationHall={}", req.getServletPath(),JSON.toJSONString(examinationHall));
        CommonResult<String> result = new CommonResult<>();
        try {
            examinationHallService.updateExamAndInvigilators(examinationHall, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除考试信息
     * @apiNote 后台管理-删除考试信息，考试信息的删除会级联删除与该考试对应的专辑信息、考生信息、考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-04 13:43:24
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delById(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                        HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            examinationHallService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除考试信息
     * @apiNote 后台管理-批量删除考试信息，考试信息的批量删除会级联批量删除与该考试对应的专辑信息、考生信息、考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examinationHallIds 考试信息ID，对应考试信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-04 14:01:43
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId,
                                              @RequestParam("examinationHallIds") String examinationHallIds, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            examinationHallService.batchDelete(examinationHallIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


}
