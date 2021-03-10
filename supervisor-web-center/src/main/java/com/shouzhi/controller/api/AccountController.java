package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.CredentialsUtil;
import com.shouzhi.basic.utils.DesensitizedUtil;
import com.shouzhi.basic.utils.JWTUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.pojo.db.SecurityQuestionSet;
import com.shouzhi.pojo.db.SecurityQuestions;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.JwtTokenVo;
import com.shouzhi.pojo.vo.RetrievePasswordVo;
import com.shouzhi.service.common.BaseService;
import com.shouzhi.service.common.EmailService;
import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.service.constants.RedisConst;
import com.shouzhi.service.constants.SecretKeyConst;
import com.shouzhi.service.interf.db.IBasicAuthService;
import com.shouzhi.service.interf.db.ILogLoginService;
import com.shouzhi.service.interf.db.ISecurityQuestionSetService;
import com.shouzhi.service.interf.db.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 匿名-登录相关操作接口
 * @author WX
 * @date 2020-06-28 14:25:25
 */
@RestController
@RequestMapping("/public/api/v1/account")
public class AccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplateService redisTemplateService;
    @Autowired
    private IBasicAuthService basicAuthService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISecurityQuestionSetService securityQuestionSetService;

    @Autowired
    private ILogLoginService logLoginService;

    @Autowired
    BaseService baseService;

    @Resource
    private DefaultKaptcha captchaProducer;
    @Autowired
    EmailService emailService;


    /**
     * 匿名-账号密码登录
     * @apiNote 账号密码登录，注：需在请求头中携带captchaToken，具体使用方法请查阅图片验证码接口
     * @param username 用户名/职工号/学号
     * @param password 密码
     * @param validateCode 验证码
     * @author WX
     * @date 2020-06-29 09:36:25
     */
    @PostMapping("/login")
    public CommonResult<JwtTokenVo> login(@RequestParam("username") String username, @RequestParam("password") String password,
                                          @RequestParam("validateCode") String validateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<JwtTokenVo> result = new CommonResult<>();
        try {
            Map<String, Object> validateMap = baseService.doPicValidateCode(req, validateCode);
            if(!((boolean) validateMap.get("isValidate"))){
                logLoginService.save("0","102","",username,req);
                return result.setErrorResult((ErrorCodeEnum)validateMap.get("errorCodeEnum"));
            }
            // RSA私钥解密密码为明文
            password = baseService.decryptData(password);

            // 校验用户名、密码
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserName(username);
            basicAuth.setPassWord(CredentialsUtil.MD5Pwd(password, username, SecretKeyConst.MD5_HASH_ITERATIONS));
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if(basicAuth==null){
                // 校验编号、密码
                basicAuth = new BasicAuth();
                basicAuth.setPersonNum(username);
                basicAuth = basicAuthService.selectOneByParam(basicAuth);
                if(basicAuth==null || !basicAuth.getPassWord().equals(CredentialsUtil.MD5Pwd(password, basicAuth.getSalt(), SecretKeyConst.MD5_HASH_ITERATIONS))){
                    logLoginService.save("0","101","",username,req);
                    return result.setErrorResult(ErrorCodeEnum.USERNAME_PWD_ERROR);
                }
            }
            if("1".equals(basicAuth.getIsLocked())){
                return result.setErrorResult(ErrorCodeEnum.CURRENT_USER_LOCKED_ERROR);
            }
            this.toLogin(basicAuth, username, result, req);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 匿名-邮箱验证码登录
     * @apiNote 邮箱验证码登录，注：需在请求头中携带captchaToken，具体使用方法请查阅图片验证码接口
     * @param userEmail 用户邮箱
     * @param picValidateCode 图片验证码
     * @param mailValidateCode 邮箱验证码
     * @author WX
     * @date 2020-07-13 20:14:50
     */
    @PostMapping("/loginMail")
    public CommonResult<JwtTokenVo> loginMail(@RequestParam("userEmail") String userEmail,
                                              @RequestParam("picValidateCode") String picValidateCode,
                                          @RequestParam("mailValidateCode") String mailValidateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<JwtTokenVo> result = new CommonResult<>();
        try {
            // 校验图片验证码
            Map<String, Object> picValidateMap = baseService.doPicValidateCode(req, picValidateCode);
            if(!((boolean) picValidateMap.get("isValidate"))){
                logLoginService.save("0","102","",userEmail,req);
                return result.setErrorResult((ErrorCodeEnum)picValidateMap.get("errorCodeEnum"));
            }
            // 校验邮箱验证码
            Map<String, Object> mailValidateMap = baseService.doMailValidateCode(userEmail, mailValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                logLoginService.save("0","102","",userEmail,req);
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            // 根据邮箱查询用户信息
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserEmail(userEmail);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if(basicAuth==null){
                logLoginService.save("0","101","",userEmail,req);
                return result.setErrorResult(ErrorCodeEnum.USERNAME_PWD_ERROR);
            }
            if("1".equals(basicAuth.getIsLocked())){
                return result.setErrorResult(ErrorCodeEnum.CURRENT_USER_LOCKED_ERROR);
            }
            this.toLogin(basicAuth, userEmail, result, req);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    private void toLogin(BasicAuth basicAuth, String username, CommonResult<JwtTokenVo> result, HttpServletRequest req) throws Exception {
        SysUser sysUser = new SysUser();
        sysUser.setId(basicAuth.getSysUserId());
        sysUser = sysUserService.selectByLogin(sysUser);
        Assert.notNull(sysUser,"未找到该账号关联的用户信息");
        basicAuth.setSysUser(sysUser);

        // 生成TOKEN
        JwtTokenVo jwtTokenVo = this.jwtTokenVo(basicAuth.getUserName());
        // 登录时将BasicAuth对象放入redis,时效和accessToken一样
        redisTemplateService.setStr(RedisConst.INFO_USER(basicAuth.getUserName()), JSON.toJSONString(basicAuth), JWTUtil.ACCESS_TOKEN_EXPIRE/1000);

        basicAuth.setHeadImgUrl(sysUser.getHeadImgUrl());
        basicAuth.setPersonName(sysUser.getPersonName());
        // 查询是否有密保信息[无密保说明注册成功后直接关闭了页面没有初始化身份，提示前端跳转初始化身份]
        SecurityQuestionSet sqs = new SecurityQuestionSet();
        sqs.setBasicAuthId(basicAuth.getId());
        List<SecurityQuestionSet> sqsList = securityQuestionSetService.queryListByPage(sqs);
        if(sqsList.size()>0){
            basicAuth.setHasSecurityQuestion("1");
        }else {
            // 生成设置密保的密钥，因为会跳至初始化身份页面设置密保
            redisTemplateService.setStr(RedisConst.UPDATE_SECURITY_KEY(basicAuth.getId()), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
        }
        basicAuth.setUserEmail(DesensitizedUtil.maskEmail(basicAuth.getUserEmail()));
        basicAuth.setUserMobile(DesensitizedUtil.maskMobile(basicAuth.getUserMobile()));

        //更新登录时间
        BasicAuth ba = new BasicAuth();
        ba.setId(basicAuth.getId());
        ba.setLastLoginTime(new Date());
        basicAuthService.updateByPrimaryKeySelective(ba);

        logLoginService.save("1","200",basicAuth.getId(),basicAuth.getUserName(),req);
        jwtTokenVo.setBasicAuthInfo(basicAuth);
        result.setStatus(1).setMsg("登录成功").setResultBody(jwtTokenVo);
    }


    /**
     * 匿名-发送邮箱验证码-登录|找回密码
     * @apiNote 发送邮箱验证码-登录|找回密码专用，<b>原因：会校验邮箱是否存在</b>，可能有20-30s阻塞时间，并非超时，前端记得设置请求的超时时间。
     *          注：1分钟内不可重复操作发送按钮，发送后应立即将按钮置为Disable，60S后恢复Enable
     * @param userEmail 用户邮箱，必填参数
     * @author WX
     * @date 2020-07-14 19:25:43
     */
    @PostMapping("/sendMailCode")
    public CommonResult<String> sendMailCode(@RequestParam("userEmail") String userEmail, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 校验邮箱是否存在
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserEmail(userEmail);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if (basicAuth!=null){
                baseService.sendMailCode(userEmail, captchaProducer.createText());
                result.setStatus(1).setMsg("发送成功");
            }else {
                result.setErrorResult(ErrorCodeEnum.USER_EMAIL_NOT_EXIST);
            }
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
     * 匿名-注册系统账号
     * @apiNote 注册系统账号，注：需在请求头中携带captchaToken，具体使用方法请查阅图片验证码接口
     * @param username 用户名
     * @param password 密码
     * @param personNum 用户编号（例：学生有学号，教师有工号）
     * @param validateCode 验证码
     * @author WX
     * @date 2020-07-13 15:24:51
     */
    @PostMapping("/register")
    public CommonResult<String> register(@RequestParam("username") String username, @RequestParam("password") String password,
                                            @RequestParam("personNum") String personNum, @RequestParam("validateCode") String validateCode,
                                            HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 校验验证码
            Map<String, Object> validateMap = baseService.doPicValidateCode(req, validateCode);
            if(!((boolean) validateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)validateMap.get("errorCodeEnum"));
            }
            // RSA私钥解密密码为明文
            password = baseService.decryptData(password);

            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserName(username);
            basicAuth.setPassWord(password);
            basicAuth.setPersonNum(personNum);
            basicAuthService.register(basicAuth, "-", req);
            result.setStatus(1).setMsg("注册成功");
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 匿名-刷新Token
     * @apiNote 刷新Token，当accessToken过期时可利用登陆成功时返回的refreshToken来换取新的accessToken、refreshToken
     *          使用场景举例：在获取人员接口时恰巧accessToken失效，此时无需返回登录页面，可将人员接口请求挂起，先去请求/refresh接口，
     *          换取新的accessToken、refreshToken后先将其存储在本地，再发起刚刚挂起的人员接口请求，用户无感知
     * @param refreshToken 登陆成功时返回的refreshToken
     * @author WX
     * @date 2020-06-29 09:43:09
     */
    @PostMapping("/refresh")
    public CommonResult<JwtTokenVo> refresh(@RequestParam("refreshToken") String refreshToken, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<JwtTokenVo> result = new CommonResult<>();
        try {
            String username = JWTUtil.getUsername(refreshToken);
            Boolean isAccessToken = JWTUtil.getIsAccessToken(refreshToken);
            Assert.isTrue(!isAccessToken,"试图传入AccessToken伪装refreshToken");
            JWTUtil.verify(refreshToken, username, isAccessToken);
            // 验证通过重新下发 accessToken、refreshToken
            JwtTokenVo jwtTokenVo = this.jwtTokenVo(username);
            // 延长用户信息时效
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserName(username);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);

            SysUser sysUser = new SysUser();
            sysUser.setId(basicAuth.getSysUserId());
            sysUser = sysUserService.selectByLogin(sysUser);
            Assert.notNull(sysUser,"用户信息为NULL，延长时效失败！");
            basicAuth.setSysUser(sysUser);
            redisTemplateService.setStr(RedisConst.INFO_USER(username), JSON.toJSONString(basicAuth), JWTUtil.ACCESS_TOKEN_EXPIRE/1000);
            result.setStatus(1).setMsg("刷新成功").setResultBody(jwtTokenVo);
        } catch (TokenExpiredException e){
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.TOKEN_R_EXPIRED_ERROR);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }

    private JwtTokenVo jwtTokenVo (String username){
        Map<String, Object> at = JWTUtil.createAccessToken(username);
        Map<String, Object> rt = JWTUtil.createRefreshToken(username);
        JwtTokenVo jwtTokenVo = new JwtTokenVo(String.valueOf(at.get("access_token")), (Long)at.get("access_token_expire"), String.valueOf(rt.get("refresh_token")), (Long)rt.get("refresh_token_expire"));
        return jwtTokenVo;
    }



    /**
     * 匿名-找回密码-密保找回-验证账号信息
     * @apiNote 找回密码-密保找回-验证账号信息，验证通过返回账号ID和该账号已设置密保问题列表
     * @param userName 用户名/职工号/学号
     * @author WX
     * @date 2020-07-23 11:12:31
     */
    @PostMapping("/verifyNum")
    public CommonResult<RetrievePasswordVo> verifyNum(@RequestParam("userName") String userName, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<RetrievePasswordVo> result = new CommonResult<>();
        try {
            // 查询用户名是否存在
            BasicAuth basicAuth1 = new BasicAuth();
            basicAuth1.setUserName(userName);
            basicAuth1 = basicAuthService.selectOneByParam(basicAuth1);
            // 查询编号是否存在
            BasicAuth basicAuth2 = new BasicAuth();
            basicAuth2.setPersonNum(userName);
            basicAuth2 = basicAuthService.selectOneByParam(basicAuth2);

            // 不存在报错提示
            if(basicAuth1==null&&basicAuth2==null){
                return result.setErrorResult(ErrorCodeEnum.USER_NOT_FOUND);
            }
            // 因为提供的信息只有一条，为保证信息准确需查询两次，如果都查到并且两个ID不一致就报错，
            // ID                                   UserName        PersonNum   Pass
            // e1826f69fd0b4876aaf133dfd4f6edd2     zhangsan     JS20180210     true
            // e1826f69fd0b4876aaf133dfd4f6edd2     JS20180210   JS20180210     true
            // e1826f69fd0b4876aaf133dfd4f6edd2     JS20170549   JS20180210     false
            if(basicAuth1!=null&&basicAuth2!=null&&!basicAuth1.getId().equals(basicAuth2.getId())){
                return result.setErrorResult(ErrorCodeEnum.USER_FOUND_MULTIPLE);
            }

            basicAuth1 = (basicAuth1!=null?basicAuth1:basicAuth2);

            // 存在则根据id查询密保信息
            List<SecurityQuestions> sqList = basicAuthService.findSecurityQuestionListByBasicAuthId(basicAuth1.getId(), req);
            RetrievePasswordVo retrievePasswordVo = new RetrievePasswordVo(basicAuth1.getId(),sqList);
            // 返回密保信息
            result.setStatus(1).setMsg("查询成功").setResultBody(retrievePasswordVo);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 匿名-找回密码-密保找回-验证密保
     * @apiNote 找回密码-密保找回-验证密保
     *          验证通过系统会产生一个与该账号对应的临时密钥，时效15分钟，需在有效时间内调用operType与之对应的接口并完成修改
     * @param rowId 被操作记录行ID(路径参数)
     * @param securityQuestionId1 原密保问题1ID
     * @param answer1 原密保答案1
     * @param securityQuestionId2 原密保问题2ID
     * @param answer2 原密保答案2
     * @param operType 操作类型：RETRIEVE_PASSWORD(找回密码验证)
     * @author WX
     * @date 2020-07-23 15:02:34
     */
    @PostMapping("/verifySQ/{rowId}")
    public void verifySecurityQuestion(@PathVariable("rowId") String rowId,
                                       @RequestParam("securityQuestionId1") String securityQuestionId1, @RequestParam("answer1") String answer1,
                                       @RequestParam("securityQuestionId2") String securityQuestionId2, @RequestParam("answer2") String answer2,
                                       @RequestParam("operType") String operType,
                                       HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        // 请求转发，这里不能使用SpringMVC提供的简便方式转发，因为注解都是@RestController
        // return "forward:/api/v1/basicAuth/verifySQ/"+rowId;
        req.getRequestDispatcher("/api/v1/basicAuth/verifySQ/"+rowId).forward(req,resp);
    }


    /**
     * 匿名-找回密码-通用-重置密码
     * @apiNote 找回密码-密保找回|邮箱找回|手机找回-重置密码
     * @param rowId 被操作记录行ID(路径参数)
     * @param newPwd 新密码
     * @param validateCode 图片验证码
     * @author WX
     * @date 2020-07-23 15:05:43
     */
    @PutMapping("/updatePwd/{rowId}")
    public void updatePwd(@PathVariable("rowId") String rowId, @RequestParam("newPwd") String newPwd,
                          @RequestParam("validateCode") String validateCode,
                          HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        req.getRequestDispatcher("/api/v1/basicAuth/updatePwd/"+rowId).forward(req,resp);
    }


    /**
     * 匿名-找回密码-邮箱找回-验证邮箱
     * @apiNote 找回密码-邮箱找回-验证邮箱，验证通过仅返回账号ID
     * @param userEmail 用户邮箱
     * @param mailValidateCode 邮箱验证码
     * @author WX
     * @date 2020-07-23 16:07:13
     */
    @PostMapping("/verifyMail")
    public CommonResult<Map<String, String>> verifyMail(@RequestParam("userEmail") String userEmail,
                                              @RequestParam("mailValidateCode") String mailValidateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        // 注意，不建议返回Map对象，因为文档生成工具无法逆向推导！这里仅返回basicAuthId一个单数，单独写Vo没必要，但也要提前跟前端商量好
        CommonResult<Map<String, String>> result = new CommonResult<>();
        try {
            // 校验邮箱验证码
            Map<String, Object> mailValidateMap = baseService.doMailValidateCode(userEmail, mailValidateCode);
            if(!((boolean) mailValidateMap.get("isValidate"))){
                return result.setErrorResult((ErrorCodeEnum)mailValidateMap.get("errorCodeEnum"));
            }
            // 根据邮箱查询用户信息
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserEmail(userEmail);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if (basicAuth==null){
                return result.setErrorResult(ErrorCodeEnum.USER_NOT_FOUND);
            }
            // 生成修改密码的密钥
            redisTemplateService.setStr(RedisConst.UPDATE_PWD_KEY(basicAuth.getId()), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
            Map<String, String> map = new HashMap<>();
            map.put("basicAuthId", basicAuth.getId());
            result.setStatus(1).setMsg("验证成功").setResultBody(map);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 匿名-找回密码-发送手机验证码
     * @apiNote 发送手机验证码-找回密码专用，<b>原因：会校验手机是否存在</b>
     *          注：1分钟内不可重复操作发送按钮，发送后应立即将按钮置为Disable，60S后恢复Enable
     * @param userMobile 用户手机号，必填参数
     * @author WX
     * @date 2020-07-14 19:25:43
     */
    @PostMapping("/sendMobileCode")
    public CommonResult<String> sendMobileCode(@RequestParam("userMobile") String userMobile, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            // 校验手机号是否存在
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserMobile(userMobile);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if (basicAuth!=null){
                baseService.sendMobileCode(basicAuth.getId(), userMobile, captchaProducer.createText());
                result.setStatus(1).setMsg("发送成功");
            }else {
                result.setErrorResult(ErrorCodeEnum.USER_MOBILE_NOT_EXIST);
            }
        } catch (IllegalArgumentException e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 匿名-找回密码-手机找回-验证手机
     * @apiNote 找回密码-手机找回-验证手机，该接口与验证邮箱性质一样，验证通过仅返回账号ID
     * @param userMobile 用户手机
     * @param picValidateCode 图片验证码
     * @param mobileValidateCode 手机验证码
     * @author WX
     * @date 2020-07-26 16:57:29
     */
    @PostMapping("/verifyMobile")
    public CommonResult<Map<String, String>> verifyMobile(@RequestParam("userMobile") String userMobile,
                                                          @RequestParam("picValidateCode") String picValidateCode,
                                                        @RequestParam("mobileValidateCode") String mobileValidateCode, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        // 注意，不建议返回Map对象，因为文档生成工具无法逆向推导！这里仅返回basicAuthId一个单数，单独写Vo没必要，但也要提前跟前端商量好
        CommonResult<Map<String, String>> result = new CommonResult<>();
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
            // 根据手机查询用户信息
            BasicAuth basicAuth = new BasicAuth();
            basicAuth.setUserMobile(userMobile);
            basicAuth = basicAuthService.selectOneByParam(basicAuth);
            if (basicAuth==null){
                return result.setErrorResult(ErrorCodeEnum.USER_NOT_FOUND);
            }
            // 生成修改密码的密钥
            redisTemplateService.setStr(RedisConst.UPDATE_PWD_KEY(basicAuth.getId()), basicAuth.getUserName(), RedisConst.TTL_SECONDS_900);
            Map<String, String> map = new HashMap<>();
            map.put("basicAuthId", basicAuth.getId());
            result.setStatus(1).setMsg("验证成功").setResultBody(map);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }


}
