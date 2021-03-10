package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.DesensitizedUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestionSet;
import com.shouzhi.pojo.db.SecurityQuestions;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.JwtTokenVo;
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
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心-安全设置接口
 * @description 基础认证信息
 * @author WX
 * @date 2020-07-13 14:36:47
 */
@RestController
@RequestMapping("/api/v1/basicAuth")
public class BasicAuthController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBasicAuthService basicAuthService;
    @Autowired
    private ISecurityQuestionSetService securityQuestionSetService;
    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private DefaultKaptcha captchaProducer;

    @Autowired
    BaseService baseService;

    /**
     * 查询系统账号列表
     * @apiNote 查询系统账号列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param username 用户名
     * @author WX
     * @date 2020-07-13 14:37:05
     */
    /*@PostMapping("/findList/{pageNum}/{pageSize}")
    public PageInfoVo<BasicAuth> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                        @RequestParam(value="username",required=false) String username, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        Map<String, Object> record = new HashMap<>();
        record.put("userName", username);
        PageHelper.startPage(pageNum,pageSize);
        List<BasicAuth> basicAuths = basicAuthService.queryListByPage(record);
        PageInfo<BasicAuth> pageInfo = new PageInfo<>(basicAuths);
        return this.filterPage(pageInfo);
    }*/

    // 新增系统账号 在注册内实现



    /**
     * 修改系统账号信息
     * @apiNote 修改系统账号
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
     * 删除系统账号
     * @apiNote 删除系统账号
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

    /**
     * 查询单条系统账号记录
     * @apiNote 查询单条系统账号记录，根据ID
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-13 14:36:47
     */
    /*@GetMapping("/find/{rowId}")
    public CommonResult<BasicAuth> findById(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        BasicAuth basicAuth = basicAuthService.selectByPrimaryKey(rowId);
        return CommonResult.<BasicAuth>getInstance().setStatus(1).setMsg("查询成功").setResultBody(basicAuth);
    }*/

    /**
     * 查询单条系统账号记录
     * @apiNote 查询单条系统账号记录，根据ID
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-13 14:36:47
     */
    @GetMapping("/info/{rowId}")
    public CommonResult<BasicAuth> findInfoById(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        BasicAuth basicAuth = basicAuthService.selectByPrimaryKey(rowId);
        // 查询是否有密保信息
        SecurityQuestionSet sqs = new SecurityQuestionSet();
        sqs.setBasicAuthId(basicAuth.getId());
        List<SecurityQuestionSet> sqsList = securityQuestionSetService.queryListByPage(sqs);
        if(sqsList.size()>0){
            basicAuth.setHasSecurityQuestion("1");
        }
        SysUser sysUser = sysUserService.selectByPrimaryKey(basicAuth.getSysUserId());
        basicAuth.setHeadImgUrl(sysUser.getHeadImgUrl());
        basicAuth.setPersonName(sysUser.getPersonName());
        basicAuth.setUserEmail(DesensitizedUtil.maskEmail(basicAuth.getUserEmail()));
        basicAuth.setUserMobile(DesensitizedUtil.maskMobile(basicAuth.getUserMobile()));
        return CommonResult.<BasicAuth>getInstance().setStatus(1).setMsg("查询成功").setResultBody(basicAuth);
    }


    /**
     * 验证账户-验证账号密码
     * @apiNote 修改密码、手机号、注销账号、等敏感操作之前的验证，
     *          验证通过系统会产生一个与该账号对应的临时密钥，时效15分钟，需在有效时间内调用operType与之对应的接口并完成修改
     * @param rowId 被操作记录行ID(路径参数)
     * @param userName 用户名/职工号/学号
     * @param oldPwd 原密码
     * @param validateCode 图片验证码
     * @param operType 操作类型：PWD(修改密码验证)、MOBILE(绑定手机验证)、EMAIL(绑定邮箱验证)、SECURITY(绑定密保验证)、CANCEL_ACCOUNT(注销用户验证)
     * @author WX
     * @date 2020-07-13 14:36:47
     */
    @PostMapping("/verifyPwd/{rowId}")
    public CommonResult<String> verifyPwd(@PathVariable("rowId") String rowId, @RequestParam("userName") String userName,
                                                @RequestParam("oldPwd") String oldPwd, @RequestParam("validateCode") String validateCode,
                                                @RequestParam("operType") String operType, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            Map<String, Object> validateMap = baseService.doPicValidateCode(req, validateCode);
            if(!((boolean) validateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)validateMap.get("errorCodeEnum"));
            }
            // RSA私钥解密密码为明文
            oldPwd = baseService.decryptData(oldPwd);

            basicAuthService.verifyPwd(rowId, userName, oldPwd, operType, req);
            result.setStatus(1).setMsg("验证成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_OPERATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 验证账户-验证密保
     * @apiNote 修改密保问题、等敏感操作之前的验证，
     *          验证通过系统会产生一个与该账号对应的临时密钥，时效15分钟，需在有效时间内调用operType与之对应的接口并完成修改
     * @param rowId 被操作记录行ID(路径参数)
     * @param securityQuestionId1 原密保问题1ID
     * @param answer1 原密保答案1
     * @param securityQuestionId2 原密保问题2ID
     * @param answer2 原密保答案2
     * @param operType 操作类型：SECURITY(修改密保验证)、CANCEL_ACCOUNT(注销用户验证)、RETRIEVE_PASSWORD(找回密码验证)
     * @author WX
     * @date 2020-07-13 14:36:47
     */
    @PostMapping("/verifySQ/{rowId}")
    public CommonResult<String> verifySecurityQuestion(@PathVariable("rowId") String rowId,
                @RequestParam("securityQuestionId1") String securityQuestionId1, @RequestParam("answer1") String answer1,
                @RequestParam("securityQuestionId2") String securityQuestionId2, @RequestParam("answer2") String answer2,
                @RequestParam("operType") String operType, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.verifySecurityQuestion(rowId, securityQuestionId1, answer1, securityQuestionId2, answer2, operType, req);
            result.setStatus(1).setMsg("验证成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_OPERATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 验证账户-验证邮箱
     * @apiNote 修改邮箱、等敏感操作之前的验证，
     *          验证通过系统会产生一个与该账号对应的临时密钥，时效15分钟，需在有效时间内调用operType与之对应的接口并完成修改
     * @param rowId 被操作记录行ID(路径参数)
     * @param mailValidateCode 邮箱验证码
     * @param operType 操作类型：EMAIL(修改邮箱验证)、CANCEL_ACCOUNT(注销用户验证)
     * @author WX
     * @date 2020-07-17 11:04:26
     */
    @PostMapping("/verifyMail/{rowId}")
    public CommonResult<String> verifyMail(@PathVariable("rowId") String rowId,
                                           @RequestParam("mailValidateCode") String mailValidateCode,
                                           @RequestParam("operType") String operType, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            BasicAuth basicAuth = basicAuthService.selectByPrimaryKey(rowId);
            // 校验邮箱验证码
            Map<String, Object> mailValidateMap = baseService.doMailValidateCode(basicAuth.getUserEmail(), mailValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            basicAuthService.verifyMail(rowId, "-", basicAuth, operType, req);
            result.setStatus(1).setMsg("验证成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_OPERATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 验证账户-验证手机
     * @apiNote 修改手机、等敏感操作之前的验证，
     *          验证通过系统会产生一个与该账号对应的临时密钥，时效15分钟，需在有效时间内调用operType与之对应的接口并完成修改
     * @param rowId 被操作记录行ID(路径参数)
     * @param mobileValidateCode 手机验证码
     * @param operType 操作类型：MOBILE(修改手机验证)、CANCEL_ACCOUNT(注销用户验证)
     * @author WX
     * @date 2020-07-26 13:53:08
     */
    @PostMapping("/verifyMobile/{rowId}")
    public CommonResult<String> verifyMobile(@PathVariable("rowId") String rowId,
                                           @RequestParam("mobileValidateCode") String mobileValidateCode,
                                           @RequestParam("operType") String operType, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            BasicAuth basicAuth = basicAuthService.selectByPrimaryKey(rowId);
            // 校验手机验证码
            Map<String, Object> mailValidateMap = baseService.doMobileValidateCode(basicAuth.getUserMobile(), mobileValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            basicAuthService.verifyMobile(rowId, "-", basicAuth, operType, req);
            result.setStatus(1).setMsg("验证成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_OPERATE_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 修改密码
     * @apiNote 修改系统账号密码，密码一旦修改成功该账号对应的所有信息都将失效，如accessToken、refreshToken、userInfoCache等。
     *          前端需将缓存用户本地storage的accessToken、refreshToken<b>等敏感信息</b>删除。用户需重新登录后再重新缓存。
     * @param rowId 被操作记录行ID(路径参数)
     * @param newPwd 新密码
     * @param validateCode 图片验证码
     * @author WX
     * @date 2020-07-13 14:36:47
     */
    @PutMapping("/updatePwd/{rowId}")
    public CommonResult<String> updatePwd(@PathVariable("rowId") String rowId, @RequestParam("newPwd") String newPwd,
                                          @RequestParam("validateCode") String validateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            Map<String, Object> validateMap = baseService.doPicValidateCode(req, validateCode);
            if(!((boolean) validateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)validateMap.get("errorCodeEnum"));
            }
            // RSA私钥解密密码为明文
            newPwd = baseService.decryptData(newPwd);

            basicAuthService.updatePwd(rowId, "-", newPwd, req);
            result.setStatus(1).setMsg("修改成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 查询已设置个人密保问题列表
     * @apiNote 查询已设置个人密保问题列表，根据账号ID。如果未设置则返回NULL。用于验证密保。
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-21 11:31:50
     */
    @GetMapping("/findSqList/{rowId}")
    public CommonResult<List<SecurityQuestions>> findSecurityQuestionListByBasicAuthId(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        List<SecurityQuestions> sqList = basicAuthService.findSecurityQuestionListByBasicAuthId(rowId, req);
        return CommonResult.<List<SecurityQuestions>>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sqList);
    }


    /**
     * 设置/修改密保问题
     * @apiNote 设置/修改密保问题，提交时需将两条密保记录以JSON数组形式提交，本接口设置和修改皆是新增操作，id字段需忽略。
     *          如：[{"basicAuthId":"1","securityQuestionId":"8","rank":1,"answer":"sqk15q"},{"basicAuthId":"1","securityQuestionId":"9","rank":2,"answer":"sqk15q"}]
     * @param rowId 被操作记录行ID(路径参数)
     * @param securityQuestionSets 密保问题设置 集合
     * @author WX
     * @date 2020-07-16 11:46:52
     */
    @PostMapping("/saveSQ/{rowId}")
    public CommonResult<String> saveSecurityQuestionSet(@PathVariable("rowId") String rowId, HttpServletRequest req,
                                                        @RequestBody List<SecurityQuestionSet> securityQuestionSets) {
        logger.info("url={},List<SecurityQuestionSet>={}", req.getServletPath(), JSON.toJSONString(securityQuestionSets));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.saveSecurityQuestionSet(rowId, "-", securityQuestionSets, req);
            result.setStatus(1).setMsg("设置成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 发送邮箱验证码
     * @apiNote 发送邮箱验证码，<b>注：不会校验邮箱是否存在</b>，可能有20-30s阻塞时间，并非超时，前端记得设置请求的超时时间。
     *          <b>userEmail为可选参数，当传入时给指定的userEmail发送验证码。未传入时则给当前{rowId}所绑邮箱发送。</b>
     *          例：验证邮箱操作调用时无需传userEmail参数，换绑邮箱调用时则需传入userEmail参数。
     *          注：1分钟内不可重复操作发送按钮，发送后应立即将按钮置为Disable，60S后恢复Enable
     * @param rowId 被操作记录行ID(路径参数)
     * @param userEmail 用户邮箱，可选参数
     * @author WX
     * @date 2020-07-14 19:25:43
     */
    @PostMapping("/sendMailCode/{rowId}")
    public CommonResult<String> sendMailCode(@PathVariable("rowId") String rowId, HttpServletRequest req,
                                             @RequestParam(value="userEmail",required=false) String userEmail) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.sendMailCode(rowId, userEmail, captchaProducer.createText(), req);
            result.setStatus(1).setMsg("发送成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("邮箱验证码发送失败-"+e.getMessage());
            result.setErrorResult(ErrorCodeEnum.USER_EMAIL_SEND_ERROR);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 绑定/修改邮箱
     * @apiNote 绑定/修改邮箱，注：需在请求头中携带captchaToken，具体使用方法请查阅图片验证码接口
     * @param rowId 被操作记录行ID(路径参数)
     * @param userEmail 用户邮箱
     * @param picValidateCode 图片验证码
     * @param mailValidateCode 邮箱验证码
     * @author WX
     * @date 2020-07-13 20:14:50
     */
    @PutMapping("/updateMail/{rowId}")
    public CommonResult<String> updateMail(@PathVariable("rowId") String rowId, @RequestParam("userEmail") String userEmail,
                                              @RequestParam("picValidateCode") String picValidateCode,
                                              @RequestParam("mailValidateCode") String mailValidateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 校验图片验证码
            Map<String, Object> picValidateMap = baseService.doPicValidateCode(req, picValidateCode);
            if(!((boolean) picValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)picValidateMap.get("errorCodeEnum"));
            }
            // 校验邮箱验证码
            Map<String, Object> mailValidateMap = baseService.doMailValidateCode(userEmail, mailValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            basicAuthService.updateMail(rowId, "-", userEmail, req);
            result.setStatus(1).setMsg("设置成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 解绑邮箱
     * @apiNote 解绑邮箱
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-17 14:46:13
     */
    @PutMapping("/delMail/{rowId}")
    public CommonResult<String> delMail(@PathVariable("rowId") String rowId, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.delMail(rowId, "-", req);
            result.setStatus(1).setMsg("解绑成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 发送手机验证码
     * @apiNote 发送手机验证码，<b>注：不会校验手机号是否存在</b>
     *          <b>userMobile为可选参数，当传入时给指定的userMobile发送验证码。未传入时则给当前{rowId}所绑手机发送。</b>
     *          例：验证手机操作调用时无需传userMobile参数，换绑手机调用时则需传入userMobile参数。
     *          注：1分钟内不可重复操作发送按钮，发送后应立即将按钮置为Disable，60S后恢复Enable
     * @param rowId 被操作记录行ID(路径参数)
     * @param userMobile 用户手机号，可选参数
     * @author WX
     * @date 2020-07-24 16:25:07
     */
    @PostMapping("/sendMobileCode/{rowId}")
    public CommonResult<String> sendMobileCode(@PathVariable("rowId") String rowId, HttpServletRequest req,
                                             @RequestParam(value="userMobile",required=false) String userMobile) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.sendMobileCode(rowId, userMobile, captchaProducer.createText(), req);
            result.setStatus(1).setMsg("发送成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 绑定/修改手机
     * @apiNote 绑定/修改手机，注：需在请求头中携带captchaToken，具体使用方法请查阅图片验证码接口
     * @param rowId 被操作记录行ID(路径参数)
     * @param userMobile 用户手机号码
     * @param picValidateCode 图片验证码
     * @param mobileValidateCode 手机验证码
     * @author WX
     * @date 2020-07-24 20:14:50
     */
    @PutMapping("/updateMobile/{rowId}")
    public CommonResult<String> updateMobile(@PathVariable("rowId") String rowId, @RequestParam("userMobile") String userMobile,
                                           @RequestParam("picValidateCode") String picValidateCode,
                                           @RequestParam("mobileValidateCode") String mobileValidateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 校验图片验证码
            Map<String, Object> picValidateMap = baseService.doPicValidateCode(req, picValidateCode);
            if(!((boolean) picValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)picValidateMap.get("errorCodeEnum"));
            }
            // 校验手机验证码
            Map<String, Object> mailValidateMap = baseService.doMobileValidateCode(userMobile, mobileValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            basicAuthService.updateMobile(rowId, "-", userMobile, req);
            result.setStatus(1).setMsg("设置成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 解绑手机
     * @apiNote 解绑手机
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-28 11:27:53
     */
    @PutMapping("/delMobile/{rowId}")
    public CommonResult<String> delMobile(@PathVariable("rowId") String rowId, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.delMobile(rowId, "-", req);
            result.setStatus(1).setMsg("解绑成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 注销账号
     * @apiNote 注销账号，该接口依赖前两部的验证操作，单独调用不会成功。防止接口被非法抓包时绕过验证步骤直接调用该接口。
     *          一旦注销成功该账号对应的所有信息都将失效，如accessToken、refreshToken、userInfoCache等。
     *          前端需将缓存用户本地storage的accessToken、refreshToken<b>等敏感信息</b>删除。
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-13 15:05:20
     */
    @PutMapping("/cancel/{rowId}")
    public CommonResult<String> cancelAccount(@PathVariable("rowId") String rowId,
                                          HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            basicAuthService.cancelAccount(rowId, "-", req);
            result.setStatus(1).setMsg("注销成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }

    // TODO 对于无状态的JWT 退出该如何做？ 和更改密码一样,清除redis中对应的User信息？
}
