package com.shouzhi.pojo.vo;

import com.shouzhi.pojo.db.BasicAuth;

import java.io.Serializable;

/**
 * JwtTokenVo，用于返回前端使用，便于ApiDoc逆向生成可读性文档
 * @author WX
 * @date 2020-06-28 16:36:45
 */
public class JwtTokenVo implements Serializable{

    /**
     * accessToken每次请求私有API时(即非“/public/**”URI)需在request header中携带，用于Server鉴权
     * headerName=Authorization，headerValue=服务器响应的accessToken串
     */
    private String accessToken;

    /**
     * accessToken过期时间，时效很短，比如：1H、2H，严格环境一般都在30Min内(网银等)
     */
    private Long accessTokenExpire;

    /**
     * refreshToken使用方法与accessToken一致，但无需每次携带，用于获取最新的accessToken、refreshToken。具体见刷新Token接口
     */
    private String refreshToken;

    /**
     * refreshToken过期时间，时效很长，比如：15DAY、30DAY
     */
    private Long refreshTokenExpire;

    /**
     * 账号基础认证信息
     */
    private BasicAuth basicAuthInfo;

    private static final long serialVersionUID = 1L;

    public JwtTokenVo() {
    }

    public JwtTokenVo(String accessToken, Long accessTokenExpire, String refreshToken, Long refreshTokenExpire) {
        this.accessToken = accessToken;
        this.accessTokenExpire = accessTokenExpire;
        this.refreshToken = refreshToken;
        this.refreshTokenExpire = refreshTokenExpire;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(Long accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(Long refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
    }

    public BasicAuth getBasicAuthInfo() {
        return basicAuthInfo;
    }

    public void setBasicAuthInfo(BasicAuth basicAuthInfo) {
        this.basicAuthInfo = basicAuthInfo;
    }
}
