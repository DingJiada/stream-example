package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.SignUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.Examinee;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.common.ConsumerWrapper;
import com.shouzhi.service.interf.db.IExamineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;

/**
 * 云监考-后台-考生信息管理接口
 * @author WX
 * @date 2020-08-04 17:20:43
 */
@RestController
@RequestMapping("/api/v1/examinee")
public class ExamineeController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IExamineeService examineeService;

    /**
     * 后台管理-查询考生列表
     * @apiNote 后台管理-查询考生列表，需将上层[考试管理]中的<b>考试(考场)id</b>带过来，当作此接口的必要查询参数
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param examinationHallId 考试id，必填参数
     * @param studentCodeOrName 考生学号或考生姓名
     * @author WX
     * @date 2020-08-04 17:22:43
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<Examinee>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                       @RequestParam(value="examinationHallId",required=true) String examinationHallId,
                                                       @RequestParam(value="studentCodeOrName",required=false) String studentCodeOrName,
                                                       HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<Examinee>> result = new CommonResult<>();
        Examinee examinee = new Examinee();
        examinee.setExaminationHallId(examinationHallId);
        examinee.setStudentCodeOrName(studentCodeOrName);
        PageHelper.startPage(pageNum,pageSize);
        List<Examinee> examinees = examineeService.queryListByPage(examinee);
        PageInfo<Examinee> pageInfo = new PageInfo<>(examinees);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增考生信息
     * @apiNote 后台管理-新增考生信息，需将上层[考试管理]中的<b>考试(考场)id</b>和<b>考场编号</b>带过来，当作此接口的必要新增参数
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examinee
     * @author WX
     * @date 2020-08-05 09:00:42
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveExaminee(@PathVariable("permId") String permId,
                                             @RequestBody Examinee examinee, HttpServletRequest req) {
        logger.info("url={},examinee={}", req.getServletPath(),JSON.toJSONString(examinee));
        CommonResult<String> result = new CommonResult<>();
        try {
            examineeService.saveExaminee(examinee, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_INSERT_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改考生信息
     * @apiNote 后台管理-修改考生信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examinee
     * @author WX
     * @date 2020-08-05 10:10:42
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateExaminee(@PathVariable("permId") String permId,
                                                      @RequestBody Examinee examinee, HttpServletRequest req) {
        logger.info("url={},examinee={}", req.getServletPath(),JSON.toJSONString(examinee));
        CommonResult<String> result = new CommonResult<>();
        try {
            examineeService.updateExaminee(examinee, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除考生信息
     * @apiNote 后台管理-删除考生信息，考生信息的删除会级联删除与该考生对应的考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-05 10:19:42
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delById(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                        HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            examineeService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除考生信息
     * @apiNote 后台管理-批量删除考生信息，考生信息的批量删除会级联批量删除与该考生对应的考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examineeIds 考生信息ID，对应考生信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-04 14:01:43
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId,
                                              @RequestParam("examineeIds") String examineeIds, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            examineeService.batchDelete(examineeIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }

}
