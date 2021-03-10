package com.shouzhi.controller.api.foreground;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.SignUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.ExaminationHall;
import com.shouzhi.pojo.db.Examinee;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.ConsumerWrapper;
import com.shouzhi.service.interf.db.IExamVideoService;
import com.shouzhi.service.interf.db.IExaminationHallService;
import com.shouzhi.service.interf.db.IExamineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 前端平台-云监考-考试管理接口
 * @author WX
 * @date 2020-08-05 18:28:28
 */
@RestController
@RequestMapping("/api/v1/fg/examinationHall")
public class FgExaminationHallController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IExaminationHallService examinationHallService;

    @Autowired
    private IExamVideoService examVideoService;

    @Autowired
    private IExamineeService examineeService;

    @Autowired
    private BaseService baseService;

    /**
     * 前端平台-查询考试列表
     * @apiNote 前端平台-云监考-查询考试列表，仅显示当前登陆人下的云监考考试列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param sysUserId 系统用户id，必填参数
     * @author WX
     * @date 2020-08-05 18:32:43
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<ExaminationHall>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                              @RequestParam("sysUserId") String sysUserId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<ExaminationHall>> result = new CommonResult<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysUserId", sysUserId);
        // map.put("examStatusNEQ", "-1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("endTimeGTEQ", simpleDateFormat.format(new Date()));
        PageHelper.startPage(pageNum,pageSize);
        List<ExaminationHall> examinationHalls = examinationHallService.selectByUserParam(map);
        PageInfo<ExaminationHall> pageInfo = new PageInfo<>(examinationHalls);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 前端平台-考场详情
     * @apiNote 前端平台-云监考-考场详情-查询考生列表，需将上层[考试列表]中的<b>考试(考场)id</b>带过来，当作此接口的必要查询参数
     *          本接口返回的每条考生数据内的直播地址应该是可播放的，所以下方<b>hasSign参数</b>应传1，即：hasSign="1"
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param examinationHallId 考试id，必填参数
     * @param studentCodeOrName 考生学号或考生姓名
     * @param hasSign 是否生成带sign的播放地址，"1"为是，"0"为否
     * @author WX
     * @date 2020-08-04 17:22:43
     */
    @PostMapping("/examineeList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<Examinee>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                       @RequestParam(value="examinationHallId",required=true) String examinationHallId,
                                                       @RequestParam(value="studentCodeOrName",required=false) String studentCodeOrName,
                                                       @RequestParam(value="hasSign",required=false) String hasSign,
                                                       HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<Examinee>> result = new CommonResult<>();
        ExaminationHall examinationHall = examinationHallService.selectByPrimaryKey(examinationHallId);
        // 设置的开始时间-当前时间 > 规定的时间 = 表示没在规定时间内
        if((examinationHall.getStartTime().getTime()-System.currentTimeMillis())>(examinationHall.getInvigilatorsLoginTime()*60*1000)){
            return result.setErrorResult(ErrorCodeEnum.EXAM_HALL_TIME_NO_STARTED_ERROR);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("examinationHallId",examinationHallId);
        map.put("studentCodeOrName",studentCodeOrName);
        PageHelper.startPage(pageNum,pageSize);
        List<Examinee> examinees = examineeService.foregroundExamineeList(map);
        if("1".equals(hasSign)){
            baseService.batchGenerateSignUrl(examinees, "liveAddressPull");
        }
        PageInfo<Examinee> pageInfo = new PageInfo<>(examinees);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 前端平台-考场详情-考试异常行为打点
     * @apiNote 前端平台-云监考-考场详情-考试异常行为打点，需将上层[考试列表]中的<b>考试(考场)id</b>带过来，当作此接口的必要查询参数
     * @param examinationHallId 考试(考场)id
     * @param examineeId 考生信息id
     * @author WX
     * @date 2020-08-15 19:17:26
     */
    @PostMapping("/addExamAbnormal")
    public CommonResult<String> addExamAbnormal(@RequestParam("examinationHallId") String examinationHallId,
                                                @RequestParam("examineeId") String examineeId, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            examVideoService.addExamAbnormal(examinationHallId, examineeId, "-", req);
            result.setStatus(1).setMsg("打点成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
