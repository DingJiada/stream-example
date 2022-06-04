package com.shouzhi.service.common;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.constants.FileTypeConstants;
import com.shouzhi.basic.utils.*;
import com.shouzhi.pojo.db.BasicAuth;
import com.shouzhi.service.constants.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * BaseService
 * @author WX
 * @date 2020-06-30 15:26:15
 */
@Service("baseService")
@Transactional(rollbackFor = {Exception.class, Error.class}) //该类下所有的方法都受事务控制,需要注意的是：对方法进行try-catch后 捕捉异常,则事物就会失效,需要使用TransactionAspectSupport类在catch内手动回滚
public class BaseService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String TOKEN_HEADER_KEY = "Authorization"; //Bearer
    private static String CAPTCHA_TOKEN_HEADER_KEY = "captchaToken"; //Bearer
    private static String VALIDATE_CODE_ERROR_TYPE_INCORRECT = "incorrect"; // 不正确
    private static String VALIDATE_CODE_ERROR_TYPE_EXPIRED = "expired"; // 过期

    @Autowired
    RedisTemplateService redisTemplateService;

    @Autowired
    EmailService emailService;

    @Autowired  //@Qualifier("customProperties")
    private Properties customProperties;

    /**
     * 获取当前登录用户的信息
     * @param req
     * @author WX
     * @date 2020-06-30 16:13:09
     */
    public BasicAuth getUserInfo(HttpServletRequest req) throws Exception {
        String username = JWTUtil.getUsername(req.getHeader(TOKEN_HEADER_KEY));
        String userInfo = redisTemplateService.getStr(RedisConst.INFO_USER(username));
        return JSON.parseObject(userInfo, BasicAuth.class);
    }

    //-----------------------------------------验证码是否正确 Start-------------------------------------------------
    /**
     * 校验验证码是否正确，正确isValidate=true，错误isValidate=false,errorCodeEnum=对应错误码枚举
     * @param req
     * @param validateCode
     * @author WX
     * @date 2020-07-13 17:52:40
     */
    public Map<String, Object> doPicValidateCode(HttpServletRequest req, String validateCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String headerCaptchaToken = req.getHeader(CAPTCHA_TOKEN_HEADER_KEY);
        if(headerCaptchaToken==null){
            map.put("isValidate", false);
            map.put("errorCodeEnum", ErrorCodeEnum.CAPTCHA_HEADER_NOT_FOUND_ERROR);
            return map;
        }
        // 根据CaptchaToken去redis查询验证码
        map = this.validateCode(headerCaptchaToken, validateCode, "pic");
        return map;
    }

    /**
     * 校验邮箱验证码是否正确，正确isValidate=true，错误isValidate=false,errorCodeEnum=对应错误码枚举
     * @param userEmail
     * @param validateCode
     * @author WX
     * @date 2020-07-14 19:46:23
     */
    public Map<String, Object> doMailValidateCode(String userEmail, String validateCode) throws Exception {
        String key = RedisConst.EMAIL_VALID_CODE(userEmail);
        return this.validateCode(key, validateCode, "mail");
    }

    /**
     * 校验手机证码是否正确，正确isValidate=true，错误isValidate=false,errorCodeEnum=对应错误码枚举
     * @param userMobile
     * @param validateCode
     * @author WX
     * @date 2020-07-26 11:52:05
     */
    public Map<String, Object> doMobileValidateCode(String userMobile, String validateCode) throws Exception {
        String key = RedisConst.MOBILE_VALID_CODE(userMobile);
        return this.validateCode(key, validateCode, "mobile");
    }

    private Map<String, Object> validateCode(String key, String validateCode, String type){
        Map<String, Object> map = new HashMap<>();
        // 根据key去redis查询验证码
        if(redisTemplateService.hasKey(key)){
            if(validateCode.toLowerCase().equals(redisTemplateService.getStr(key))){
                redisTemplateService.delete(key);
                map.put("isValidate", true);
            }else {
                map.put("isValidate", false);
                this.putValidateCodeError(map, type, VALIDATE_CODE_ERROR_TYPE_INCORRECT);
            }
        }else {
            map.put("isValidate", false);
            this.putValidateCodeError(map, type, VALIDATE_CODE_ERROR_TYPE_EXPIRED);
        }
        return map;
    }
    private void putValidateCodeError(Map<String, Object> map, String validateType, String errorType){
        switch (validateType){
            case "pic":
                map.put("errorCodeEnum", VALIDATE_CODE_ERROR_TYPE_INCORRECT.equals(errorType)?ErrorCodeEnum.CAPTCHA_ERROR:ErrorCodeEnum.CAPTCHA_EXPIRED_ERROR);
                break;
            case "mail":
                map.put("errorCodeEnum", VALIDATE_CODE_ERROR_TYPE_INCORRECT.equals(errorType)?ErrorCodeEnum.MAIL_CAPTCHA_ERROR:ErrorCodeEnum.MAIL_CAPTCHA_EXPIRED_ERROR);
                break;
            case "mobile":
                map.put("errorCodeEnum", VALIDATE_CODE_ERROR_TYPE_INCORRECT.equals(errorType)?ErrorCodeEnum.MOBILE_CAPTCHA_ERROR:ErrorCodeEnum.MOBILE_CAPTCHA_EXPIRED_ERROR);
                break;
        }
    }
    //-----------------------------------------验证码是否正确 End-------------------------------------------------

    /**
     * 发送邮件验证码
     * @param userEmail
     * @param capText
     * @throws MessagingException
     */
    public void sendMailCode(String userEmail, String capText) throws MessagingException {
        redisTemplateService.setStr(RedisConst.EMAIL_VALID_CODE(userEmail), capText, RedisConst.TTL_SECONDS_900);
        // 发送验证码
        String subject = "邮箱验证码";
        Map<String, Object> paramMap = new HashMap(); // 模板数据
        paramMap.put("validateCode", capText);
        Map<String, String> imgMap = new HashMap<>(); // 模板图片
        String basePath = this.getClass().getClassLoader().getResource("").getPath();
        // 模板内cid写法不兼容QQ邮箱，采用公网图片地址
        // imgMap.put("email_valid_code_bg", basePath+"static/img/jmail/email_valid_code_bg.png");
        // imgMap.put("email_valid_code_logo", basePath+"static/img/jmail/email_valid_code_logo.png");
        emailService.sendTemplateMailInline(userEmail, subject, paramMap, "jmail/emailValidateCode",imgMap);
    }

    /**
     * 发送手机验证码
     * @param rowId
     * @param userMobile
     * @param capText
     * @throws MessagingException
     */
    public void sendMobileCode(String rowId, String userMobile, String capText) throws Exception {
        redisTemplateService.setStr(RedisConst.MOBILE_VALID_CODE(userMobile), capText, RedisConst.TTL_SECONDS_900);
        // 发送验证码
        String[] datas = {capText,String.valueOf(RedisConst.TTL_SECONDS_900/60)};
        HashMap<String, Object> result = MobileSMSUtil.sendValidateCode(userMobile, datas);
        if("000000".equals(result.get("statusCode"))){
            // TODO 写入数据库 {"dateCreated":"20130201155306","smsMessageSid":" ff8080813c373cab013c94b0f0512345"}
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
        }else{
            logger.info("手机验证码发送失败，账号ID={}，手机号={}，错误码={}，错误信息={}",rowId,userMobile,result.get("statusCode"),result.get("statusMsg"));
            throw new IllegalArgumentException("USER_MOBILE_SEND_ERROR");
        }
    }


    /**
     * 是否拥有一个有效的验证redisKey，有则返回val，无则抛异常
     * @param key
     * @author WX
     * @date 2020-07-17 15:03:05
     */
    public String hasVerifyKey(String key) throws IllegalArgumentException{
        Assert.isTrue(redisTemplateService.hasKey(key), "USER_NOT_FOUND");
        String username = redisTemplateService.getStr(key);
        redisTemplateService.delete(key);
        return username;
    }

    /**
     * RSA私钥解密 密文 为明文
     * @param data
     * @author WX
     * @date 2020-07-28 10:48:19
     */
    public String decryptData(String data) throws Exception {
        String rsaPrivateKey = customProperties.getProperty("rsa.privateKey");
        data = RSAUtils.decryptDataOnJava(data, rsaPrivateKey);
        // logger.info("解密后的明文data={}", data);
        return data;
    }


    //-----------------------------------------生成带Sign签名的Url Start-------------------------------------------------
    /**
     * 批量生成带Sign签名的Url
     * @param data 将要生成的List数据对象
     * @param fieldNames List数据对象中单个对象的字段名称，即：将要生成带Sign签名的字段名称。
     *                   支持嵌套字段名称，多级之间使用英文.分隔，如：schDeviceFlv1.playAddr
     * @author WX
     * @date 2021-01-04 15:19:05
     */
    public void batchGenerateSignUrl(List<?> data, String... fieldNames){
        data.parallelStream().forEach(ConsumerWrapper.throwingConsumerWrapper(e -> this.generateSignUrl(e, e.getClass(), fieldNames)));
    }


    /**
     * 生成带Sign签名的Url
     * @param data 将要生成的数据对象
     * @param clazz 参数一data的Class
     * @param fieldNames 数据对象中的字段名称，即：将要生成带Sign签名的字段名称。
     *                   支持嵌套字段名称，多级之间使用英文.分隔，如：schDeviceFlv1.playAddr
     * @author WX
     * @date 2021-01-04 14:20:39
     */
    public void generateSignUrl(Object data, Class<?> clazz, String... fieldNames) {
        if(fieldNames.length<=0) return;
        Arrays.asList(fieldNames).parallelStream().forEach(ConsumerWrapper.throwingConsumerWrapper(f -> {
            // 判断当前字段是否包含多级嵌套的model对象字段，有则获取到最后一个再处理，无则直接处理
            if(f.contains(".")){
                this.getSubAttributeField(data, clazz, f);
            }else {
                Field field = clazz.getDeclaredField(f);
                this.doGenerateSignUrl(field, data);
            }
        }));
    }

    /**
     * 获取子属性字段，级联嵌套字段
     * @param data
     * @param clazz
     * @param nestedFieldName 嵌套字段名称，多级之间使用英文.分隔，如：schDeviceFlv1.playAddr
     * @author WX
     * @date 2021-01-06 11:01:23
     */
    private void getSubAttributeField(Object data, Class<?> clazz, String nestedFieldName) throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
        String[] splitFieldNames = nestedFieldName.split("\\.");
        // 字段永远是最后一级的字段，data永远是最后一级字段归属的对象，而不是字段本身对象
        Field field = null;
        for (int i = 0; i < splitFieldNames.length; i++) {
            String splitFieldName = splitFieldNames[i];
            field = clazz.getDeclaredField(splitFieldName);
            // 如果是最后一级，不再获取字段的内容及字段所属的Class，因为外层还需要做业务处理，
            if(i!=(splitFieldNames.length-1)){
                field.setAccessible(true);
                data = field.get(data);
                clazz = data.getClass();
            }
        }
        this.doGenerateSignUrl(field, data);
    }

    /**
     * 真正处理生成签名的方法
     * (本处理方法想要在generateSignUrl中运行需要对getSubAttributeField的data参数做引用传递，但又想使用parallelStream，做了引用传递就不能使用parallelStream，所以只能单独抽出来做处理)
     * @param field
     * @param data
     * @author WX
     * @date 2021-01-06 11:01:23
     */
    private void doGenerateSignUrl(Field field, Object data) throws IllegalAccessException, MalformedURLException {
        field.setAccessible(true);
        String fieldStr = (String)field.get(data);
        if(StringUtils.isNotBlank(fieldStr)){
            // 将ws url 替换成 http url(URL类不支持ws协议)，之后获取 appName、streamName
            URL url = new URL(fieldStr.replace("ws://","http://"));
            String[] split = url.getPath().substring(1, url.getPath().lastIndexOf(".")).split("/");
            String exp = String.valueOf(System.currentTimeMillis());
            String base64UrlSign = SignUtil.base64UrlSign(split[0], split[1], exp);
            // ?exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=
            field.set(data, fieldStr+"?exp="+exp+"&sign="+base64UrlSign);
        }
    }

    //-----------------------------------------生成带Sign签名的Url End-------------------------------------------------

    /**
     * 验证导入Excel
     * @param excelFile
     * @param validRowNum excel文件内数据有效行号。比如模板内一般含有注意事项行或标题行，那这样数据在该行以下存在数据才算有效
     */
    public List<ArrayList<String>> verifyImpExcel(MultipartFile excelFile, Integer validRowNum) throws IOException, IllegalArgumentException {
        // 为空校验
        Assert.isTrue(excelFile.getSize()>0 && !excelFile.isEmpty(),"FILE_UPLOAD_EMPTY_ERROR");
        // 文件类型校验
        String extName = excelFile.getOriginalFilename().substring(excelFile.getOriginalFilename().lastIndexOf("."));
        Assert.isTrue(FileTypeConstants.XLSX.equals(extName) || FileTypeConstants.XLS.equals(extName),"FILE_UPLOAD_TYPE_ERROR");
        // 读取Excel数据到List中
        List<ArrayList<String>> ttcList = new ExcelRead().readExcel(excelFile);
        // 上传文件无效
        Assert.isTrue(ttcList!=null && ttcList.size() >= validRowNum,"FILE_UPLOAD_INVALID_ERROR");
        return ttcList;
    }
}
