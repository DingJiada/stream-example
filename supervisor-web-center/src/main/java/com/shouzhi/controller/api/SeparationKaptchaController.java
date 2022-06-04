package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.utils.UuidUtil;
import com.shouzhi.pojo.vo.CaptchaVo;
import com.shouzhi.service.common.RedisTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 匿名-图片验证码接口
 * @author WX
 * @date 2019-11-14 23:10:17
 */
@RestController
@RequestMapping("/public/api/v1/captcha")
public class SeparationKaptchaController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplateService redisTemplateService;

    @Resource
    private DefaultKaptcha captchaProducer;

    /**
     * 获取图片验证码
     * @apiNote 获取图片验证码，响应体resultBody中会有两个参数，其中captchaToken须在提交的request请求头中原样携带返回，用于Server校验。
     *          每次请求该接口都会返回新的captchaToken，无需缓存，使用后立即作废。
     *          需要注意的是，captchaToken与用户在页面填写图片验证码内对应的内容非同一回事。
     * @description 验证码图片，注：生成的Base64不可通过浏览器HTML页面复制,因为会有换行符
     * @author WX
     * @date 2019-11-14 23:42:35
     */
    @PostMapping("/generate")
    public CommonResult<CaptchaVo> generate(HttpServletRequest req, HttpServletResponse resp){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CaptchaVo captchaVo = new CaptchaVo();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();

            // create the text for the image
            String capText = captchaProducer.createText();
            System.out.println("验证码" + capText);
            // create the image with the text
            BufferedImage bi = captchaProducer.createImage(capText);
            // write the data out
            ImageIO.write(bi, "jpg", outputStream);

            // store the text in the redis
            String captchaToken = "TOKEN_CAPTCHA_"+ UuidUtil.upperCase32UUID();
            // the key expiration timeout 5 Min
            redisTemplateService.setStr(captchaToken, capText, 300);

            // ByteArray to BASE64Encoder
            BASE64Encoder encoder = new BASE64Encoder();
            //
            captchaVo.setCaptchaToken(captchaToken);
            captchaVo.setImg(encoder.encode(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CommonResult.<CaptchaVo>getInstance().setStatus(1).setMsg("生成成功").setResultBody(captchaVo);
    }
}
