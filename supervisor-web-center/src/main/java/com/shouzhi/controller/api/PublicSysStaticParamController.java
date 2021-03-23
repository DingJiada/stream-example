package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.service.interf.db.ISysStaticParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 匿名-系统静态参数查询
 * @author WX
 * @date 2020-11-24 16:50:16
 */
@RestController
@RequestMapping("/public/api/v1/sysStaticParam")
public class PublicSysStaticParamController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysStaticParamService sysStaticParamService;

    /**
     * 匿名-查询页面静态参数
     * @apiNote 匿名-查询页面静态参数，此接口应在打开某个页面时去请求，接口返回某个页面应该被显示的字段及内容值，需将其填充到对应的位置处。
     *          如用户进入登陆页面时前端应立马携带<b>pageType</b>=2请求此接口，接口返回背景轮播图URL，前端解析返回字段并填充。
     *          注：因此接口返回对象为非标准对象，接口文档的Response-fields无法自动生成，如需得知返回哪些字段可以请求一下对应接口，
     *          具体以接口返回字段为准，所有静态参数字段一般都为死字段，不会轻易变动。
     * @param pageType 页面类型，1：门户首页、2：登陆页面、3：注册页面、4：后台页眉、5：画面预览
     * @author WX
     * @date 2020-11-24 17:19:06
     */
    @PostMapping("/findPageParam")
    public CommonResult<Map<String, Object>> findPageParam(@RequestParam("pageType") String pageType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<Map<String, Object>> result = new CommonResult<>();
        Map<String, Object> pageParam = sysStaticParamService.findPageParam(pageType);
        return result.setStatus(1).setMsg("查询成功").setResultBody(pageParam);
    }

}
