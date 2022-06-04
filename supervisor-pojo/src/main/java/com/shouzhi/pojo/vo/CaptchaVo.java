package com.shouzhi.pojo.vo;

import java.io.Serializable;

/**
 * 验证码VO
 * @author WX
 * @date 2020-06-24 17:21:19
 */
public class CaptchaVo implements Serializable {

    /**
     * 验证码Token，须在提交的request请求头中原样携带返回。使用场景：登录、注册、危险操作、人机检测等等
     * headerName=captchaToken，headerValue=服务器响应的captchaToken串
     */
    private String captchaToken;

    /**
     * Base64 Img
     */
    private String img;

    private static final long serialVersionUID = 1L;

    public CaptchaVo() {
    }

    public CaptchaVo(String captchaToken, String img) {
        this.captchaToken = captchaToken;
        this.img = img;
    }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
