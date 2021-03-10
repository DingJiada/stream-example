package com.shouzhi.controller.api;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.LogOperDetail;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ILogOperDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 操作日志详情接口
 * @author WX
 * @date 2020-06-09 16:06:22
 */
@RestController
@RequestMapping("/api/v1/logOperDetail")
public class LogOperDetailController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogOperDetailService logOperDetailService;

    /**
     * 查询操作日志详情列表
     * @apiNote 查询操作日志详情列表，此列表是操作日志列表的关联表，一般与操作日志列表配合使用
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param logOperId 日志操作记录表ID
     * @author WX
     * @date 2020-06-09 09:20:34
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<LogOperDetail>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                            @RequestParam(value="logOperId",required=false) String logOperId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<LogOperDetail>> result = new CommonResult<>();
        LogOperDetail logOperDetail = new LogOperDetail();
        logOperDetail.setLogOperId(logOperId);
        PageHelper.startPage(pageNum,pageSize);
        List<LogOperDetail> logOperDetails = logOperDetailService.queryListByPage(logOperDetail);
        PageInfo<LogOperDetail> pageInfo = new PageInfo<>(logOperDetails);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

}
