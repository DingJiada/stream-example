package com.shouzhi.pojo.vo;

import com.shouzhi.pojo.db.SecurityQuestions;

import java.io.Serializable;
import java.util.List;

/**
 * 找回密码Vo
 * @author WX
 * @date 2020-07-23 13:57:11
 */
public class RetrievePasswordVo implements Serializable {

    /**
     * 账号ID
     */
    private String basicAuthId;

    /**
     * 已设置密保问题列表
     */
    private List<SecurityQuestions> securityQuestions;

    private static final long serialVersionUID = 1L;

    public RetrievePasswordVo() {
    }

    public RetrievePasswordVo(String basicAuthId, List<SecurityQuestions> securityQuestions) {
        this.basicAuthId = basicAuthId;
        this.securityQuestions = securityQuestions;
    }

    public String getBasicAuthId() {
        return basicAuthId;
    }

    public void setBasicAuthId(String basicAuthId) {
        this.basicAuthId = basicAuthId;
    }

    public List<SecurityQuestions> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(List<SecurityQuestions> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
}
