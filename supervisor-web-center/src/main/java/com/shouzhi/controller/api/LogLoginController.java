package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.LogLogin;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ILogLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录日志接口
 * @author WX
 * @date 2020-06-08 11:04:14
 */
@RestController
@RequestMapping("/api/v1/logLogin")
public class LogLoginController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogLoginService logLoginService;

    /**
     * 查询登录日志列表
     * @apiNote 查询登录日志列表，如查询张三的登录日志
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param loginIp 登录IP
     * @param loginState 登录状态，1成功，0失败
     * @param loginDate 登录日期，格式yyyy-MM-dd，如：2012-02-28
     * @param userName 用户名
     * @param personName 姓名
     * @author WX
     * @date 2020-06-09 10:20:53
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<LogLogin>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                       @RequestParam(value="loginIp",required=false) String loginIp,
                                                       @RequestParam(value="loginState",required=false) String loginState,
                                                       @RequestParam(value="loginDate",required=false) String loginDate,
                                                       @RequestParam(value="userName",required=false) String userName,
                                                       @RequestParam(value="personName",required=false) String personName,
                                                       HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<LogLogin>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("loginIp", loginIp);
        map.put("loginState", loginState);
        map.put("loginTimeLike", loginDate);
        map.put("userNameLike", userName);
        map.put("personNameLike", personName);
        PageHelper.startPage(pageNum,pageSize);
        List<LogLogin> logLoginList = logLoginService.queryListByPage(map);
        PageInfo<LogLogin> pageInfo = new PageInfo<>(logLoginList);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

}
