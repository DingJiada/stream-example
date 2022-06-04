package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.LogOper;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ILogOperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 操作日志接口
 * @author WX
 * @date 2020-06-09 14:54:56
 */
@RestController
@RequestMapping("/api/v1/logOper")
public class LogOperController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogOperService logOperService;

    /**
     * 查询操作日志列表
     * @apiNote 查询操作日志列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param tabName 被操作表的名字
     * @param operType 操作类型，1：增，2：删，3：改，4：导入
     * @author WX
     * @date 2020-06-09 09:42:50
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<LogOper>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                      @RequestParam(value="tabName",required=false) String tabName,
                                                      @RequestParam(value="operType",required=false) String operType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<LogOper>> result = new CommonResult<>();
        LogOper logOper = new LogOper();
        logOper.setTabName(tabName);
        logOper.setOperType(operType);
        PageHelper.startPage(pageNum,pageSize);
        List<LogOper> logOperList = logOperService.queryListByPage(logOper);
        PageInfo<LogOper> pageInfo = new PageInfo<>(logOperList);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

}
