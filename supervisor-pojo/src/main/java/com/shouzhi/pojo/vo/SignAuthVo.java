package com.shouzhi.pojo.vo;

import java.io.Serializable;

/**
 * Sign签名鉴权，用于返回前端使用，便于ApiDoc逆向生成可读性文档
 * @author WX
 * @date 2020-08-13 09:51:18
 */
public class SignAuthVo implements Serializable {
    /**
     * 有效期(签发时间)
     */
    private String exp;
    /**
     * sign串
     */
    private String sign;

    private static final long serialVersionUID = 1L;

    public SignAuthVo(String exp, String sign) {
        this.exp = exp;
        this.sign = sign;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
