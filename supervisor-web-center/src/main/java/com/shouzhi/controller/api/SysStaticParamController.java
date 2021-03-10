package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysStaticParam;
import com.shouzhi.service.interf.db.ISysStaticParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统静态参数设置-后台
 * @author WX
 * @date 2020-11-04 15:41:16
 */
@RestController
@RequestMapping("/api/v1/sysStaticParam")
public class SysStaticParamController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysStaticParamService sysStaticParamService;

    /**
     * 后台管理-查询设置内容/参数列表
     * @apiNote 后台管理-查询设置内容、设置参数列表，无分页；parentId为必填参数；当查询设置内容列表时parentId=根节点=1，
     *          当查询某个设置内容项下的设置参数列表时parentId=查询到的‘设置内容列表单项’的id，例如，通过parentId=1查询到
     *          [{"id":"1_1","parentId":"1","paramName":"前台页眉设置","paramVal":"","paramDesc":"","showType":"1"},...]
     *          则想查询‘前台页眉设置’下的设置参数列表时parentId=1_1
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param parentId 父节点Id
     * @author WX
     * @date 2020-11-04 15:57:21
     */
    @PostMapping("/findList")
    public CommonResult<List<SysStaticParam>> findList(@RequestParam("parentId") String parentId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SysStaticParam>> result = new CommonResult<>();
        SysStaticParam sysStaticParam = new SysStaticParam();
        sysStaticParam.setParentId(parentId);
        List<SysStaticParam> sysStaticParams = sysStaticParamService.queryListByPage(sysStaticParam);
        return result.setStatus(1).setMsg("查询成功").setResultBody(sysStaticParams);
    }

    /**
     * 后台管理-批量保存设置参数
     * @apiNote 批量保存设置参数，请求示例
     *          var sysStaticParams =[{"id":"1_4_2","paramVal":"新用户注册"},{...},...];
     *          ...省略...
     *          <b>data:sysStaticParams,</b>
     *          ...省略...
     * @param sysStaticParams 设置参数列表集合，该参数名对于前端来说不存在，容易造成误解，前端需要传的只是个数组对象
     * @author WX
     * @date 2020-11-05 09:38:21
     */
    @PostMapping("/batchUpdate")
    public CommonResult<String> batchUpdate(@RequestBody List<SysStaticParam> sysStaticParams, HttpServletRequest req) {
        logger.info("url={},sysStaticParams={}", req.getServletPath(), JSON.toJSONString(sysStaticParams));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysStaticParamService.batchUpdate(sysStaticParams);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-上传图片
     * @apiNote 后台管理-上传图片，上传成功立即保存，支持单张及多张批量上传，本接口带有文件上传，<b>请注意↑↑→Content-Type</b>
     *          请严格按照<b>imgType</b>一对一匹配上传，不可交叉上传
     * @param rowId 被操作记录行ID(路径参数)（sysStaticParamId）
     * @param imgType 图片类型，1：登录轮播图、2：注册轮播图、3：网站icon、4：前台页眉左上角logo、5：后台页眉左上角logo
     * @param imgFiles file input标签的name值
     * @param isAppend 是否追加，选填；1：是、不填或略过此参数：否；默认为否
     * @author WX
     * @date 2020-11-24 14:21:08
     */
    @PostMapping("/upload/img/{rowId}")
    public CommonResult<String> uploadImgs(@PathVariable("rowId") String rowId, @RequestParam("imgType") String imgType,
                                           @RequestParam("imgFiles") MultipartFile[] imgFiles,
                                           @RequestParam(value="isAppend", required=false) String isAppend, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysStaticParamService.uploadImgs(imgFiles, imgType, isAppend, rowId, "-", req);
            result.setStatus(1).setMsg("上传成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除图片
     * @apiNote 删除图片，单张删除，非批量删除
     * @param rowId 被操作记录行ID(路径参数)
     * @param imgRelPath 单张图片相对路径；如：在查询时后台返回/bannerPic/1.jpeg,/bannerPic/2.jpeg,/bannerPic/3.jpeg，想要删除2.jpeg，则imgRelPath=/bannerPic/2.jpeg
     * @author WX
     * @date 2020-12-18 10:15:23
     */
    @DeleteMapping("/delete/img/{rowId}")
    public CommonResult<String> delImg(@PathVariable("rowId") String rowId, @RequestParam("imgRelPath") String imgRelPath,
                                       HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysStaticParamService.delImg(rowId, imgRelPath, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
