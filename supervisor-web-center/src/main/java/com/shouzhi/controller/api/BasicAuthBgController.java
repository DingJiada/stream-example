package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.interf.db.IBasicAuthService;
import com.shouzhi.service.interf.db.ISecurityQuestionSetService;
import com.shouzhi.service.interf.db.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统账户接口
 * @description 基础认证信息-后台管理部分
 * @author WX
 * @date 2020-07-31 14:41:36
 */
@RestController
@RequestMapping("/api/v1/basicAuthBg")
public class BasicAuthBgController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBasicAuthService basicAuthService;


    /**
     * 后台管理-查询系统账户列表
     * @apiNote 查询系统账户列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param personNumOrName 真实姓名 或 用户编号（例：学生有学号，教师有工号）
     * @param isLocked 是否锁定，1：锁定，0未锁定
     * @author WX
     * @date 2020-07-31 14:45:16
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<BasicAuth>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                        @RequestParam(value="personNumOrName",required=false) String personNumOrName,
                                                        @RequestParam(value="isLocked",required=false) String isLocked, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<BasicAuth>> result = new CommonResult<>();
        Map<String, Object> record = new HashMap<>();
        record.put("personNumOrName", personNumOrName);
        record.put("isLocked", isLocked);
        PageHelper.startPage(pageNum,pageSize);
        List<BasicAuth> basicAuths = basicAuthService.queryListByPage(record);
        PageInfo<BasicAuth> pageInfo = new PageInfo<>(basicAuths);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    // 新增系统账户 在注册内实现

    /**
     * 后台管理-停用/启用系统账户
     * @apiNote 停用/启用系统账户，前端需要<b>自行判断</b>系统账户列表内<b>选中行的isLocked字段</b>，并动态传参。
     *          若当前选中行的isLocked字段为'0'则此接口isLocked参数应传'1'；反之，若为'1'则此接口isLocked参数应传'0'
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @param isLocked 是否锁定，1：锁定，0未锁定
     * @author WX
     * @date 2020-07-31 17:40:46
     */
    @PutMapping("/locked/{permId}/{rowId}")
    public CommonResult<String> lockedBasicAuth(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                           @RequestParam("isLocked") String isLocked, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.lockedBasicAuth(rowId, permId, isLocked, req);
            result.setStatus(1).setMsg("操作成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-批量停用/启用系统账户
     * @apiNote 批量停用/批量启用系统账户，当isLocked参数为'1'时则为批量停用，反之，则为批量启用
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuthIds 系统账户ID，对应系统账户列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param isLocked 是否锁定，1：锁定，0未锁定
     * @author WX
     * @date 2020-08-02 14:48:16
     */
    @PutMapping("/batchLocked/{permId}")
    public CommonResult<String> batchLockedBasicAuth(@PathVariable("permId") String permId,
                                                     @RequestParam("basicAuthIds") String basicAuthIds,
                                                     @RequestParam("isLocked") String isLocked, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.batchLockedBasicAuth(basicAuthIds, permId, isLocked, req);
            result.setStatus(1).setMsg("操作成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }  catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-重置密码
     * @apiNote 后台管理-重置密码，重置后的密码为当前选中行账户的用户编号（例：学生有学号，教师有工号）
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-02 15:56:18
     */
    @PutMapping("/resetPwd/{permId}/{rowId}")
    public CommonResult<String> resetPwd(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                         HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.resetPwd(rowId, permId, req);
            result.setStatus(1).setMsg("操作成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-注销账户
     * @apiNote 后台管理-注销账户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-02 16:33:57
     */
    @PutMapping("/cancel/{permId}/{rowId}")
    public CommonResult<String> cancelAccountBackground(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                              HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.cancelAccountBackground(rowId, permId, req);
            result.setStatus(1).setMsg("注销成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量注销账户
     * @apiNote 后台管理-批量注销账户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param basicAuthIds 系统账户ID，对应系统账户列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-02 17:04:26
     */
    @PutMapping("/batchCancel/{permId}")
    public CommonResult<String> batchCancelAccountBackground(@PathVariable("permId") String permId,
                                                             @RequestParam("basicAuthIds") String basicAuthIds,
                                                        HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.batchCancelAccountBackground(basicAuthIds, permId, req);
            result.setStatus(1).setMsg("批量注销成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 修改系统账户信息
     * @apiNote 修改系统账户
     * @param basicAuth
     * @author WX
     * @date 2020-07-13 14:46:25
     */
    /*@PutMapping("/update")
    public CommonResult<String> updateById(@RequestBody BasicAuth basicAuth, HttpServletRequest req) {
        logger.info("url={},basicAuth={}", req.getServletPath(),JSON.toJSONString(basicAuth));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 前端页面个人中心左侧菜单不是后台返回的，拿不到菜单ID
            basicAuthService.update(basicAuth, "-", req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.DB_SQL_UPDATE_ERROR);
        }
        return result;
    }*/

    /**
     * 删除系统账户
     * @apiNote 删除系统账户
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-13 14:50:40
     */
    /*@DeleteMapping("/delete/{rowId}")
    public CommonResult<String> delById(@PathVariable("rowId") String rowId,
                                            HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            // 前端页面个人中心左侧菜单不是后台返回的，拿不到菜单ID
            basicAuthService.delete(rowId, "-", req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.DB_SQL_DELETE_ERROR);
        }
        return result;
    }*/



}
