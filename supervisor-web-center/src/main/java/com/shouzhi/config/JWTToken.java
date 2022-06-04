package com.shouzhi.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author WX
 * @date 2019-11-17 14:00:55
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
