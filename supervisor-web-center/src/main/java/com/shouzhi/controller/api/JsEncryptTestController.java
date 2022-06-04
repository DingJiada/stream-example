package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.utils.RSAUtils;
import com.shouzhi.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * JsEncrypt测试接口
 * @author WX
 * @date 2020-07-28 08:40:43
 */
@RestController
@RequestMapping("/public/test")
public class JsEncryptTestController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMtFiMxfZcXz0oM048VKuma9ykbyhSfEq1PJ+v4jvPmnAxdVEL+B4pVl41Me2d+HTi/saVzc0syM/qi18ALrWSrlE5lIr4dGVEDvrL2ZhWCMfGb0/nw0ZbPxFcUW9t6r3/4MYjGlzNx89WqhpHcU9oYYA7BT2aVo4NNVpRQXXBuPAgMBAAECgYBSRIYlEMcD4rZkW9eDVcczJcTkCetSOQqOEsT+bkBhWfKqUsdAerTVejMSxP2wtfYy9x99cfHuz3GRnnDQ/adMOE2JjaIGvKEFvqO7M/NBpnPQ4Jm8fX8M2J6hR2qJSrXfT/a6n1waH2aAs3KVVt1VmRy4rTYbSWG37oZxkjwdgQJBAOnX00cINvt3nEJy+JKAsVJ4VJi8j+waaxotWMk1o3r7pPWZYzuRiN7LyRGdf+I7oCL1MOXRzWDTv0p7WCdNyTECQQDeiChsLE9DVf/UeWn/CqcuEoRciogHUt4OlWaR+W99pl1vDtBJWe1kenjkHoINS2XqEeqfHH5ad2NWzxXEwwC/AkAyIcT3u3keHBxx2ngT4GcjiQyJ2hL4yQkhCYu5RziPCxfoV0QA50hyiznF/wAFnkbMd8hN3Nas5XhjHWJeurxhAkBf2T2v+hXZ5vzeQ72IAMqpaYhhY7nbjbHfjjIzxcBj2nh8EWFddsJteOMBPrK9jzBCdZ0dE2TJCotbJK83pNZ5AkApriyMgOWl3RPi4HLCM7xoQa0mHe+/xjfSciC0N11+NsodCqX2gqbPRhut3r4ttrOZfPGgF0MTEqCO8WfQlTe/";


    /**
     * JsEncrypt测试接口
     * @apiNote 将加密完成的数据传入服务器，服务器解密后将解密的明文数据返回
     * @param encryptData 加密完成的数据
     */
    @PostMapping("/JsEncrypt")
    public CommonResult<String> JsEncrypt(@RequestParam("encryptData") String encryptData, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            String s = RSAUtils.decryptDataOnJava(encryptData, privateKey);
            logger.info("解密后的明文={}", s);
            result.setStatus(1).setMsg("解密成功").setResultBody("解密后的明文="+s);
        } catch (Exception e){
            this.fillIllegalArgResult(result, new IllegalArgumentException("EXCEPTION_EXCEPTION_ERROR"), true, true, logger);
        }
        return result;
    }

}
