package com.shouzhi.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.service.constants.RedisConst;
import com.shouzhi.basic.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WX
 * @date 2019-11-17 13:58:36
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    /*@Autowired
    private IUserService userService;*/
    @Autowired
    RedisTemplateService redisTemplateService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        Boolean isAccessToken = null;
        try {
            // 1.解密获得username,初步校验
            String username = JWTUtil.getUsername(token);
            isAccessToken = JWTUtil.getIsAccessToken(token);
            if (username == null || isAccessToken==null) {
                throw new AuthenticationException(new RuntimeException("1102002"));
            }
            // 2.校验token(包括合法性与过期性)
            JWTUtil.verify(token, username, isAccessToken);

            // 3.用户信息有效性
            Boolean hasKey = redisTemplateService.hasKey(RedisConst.INFO_USER(username));
            if(!hasKey){
                throw new AuthenticationException(new RuntimeException("1102003"));
            }


        } catch (TokenExpiredException e){
            // 若是access_token失效 返回状态码，示意去刷新token
            if(isAccessToken){
                throw new AuthenticationException(new RuntimeException("1102001"));
            }else {
                throw new AuthenticationException(new RuntimeException("1102004"));
            }
        } catch (AuthenticationException e){
            throw e;
        } catch (Exception e){
            throw new AuthenticationException(new RuntimeException("1102002"));
        }

        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("————权限认证————");
        String username = JWTUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /*//获得该用户角色
        String role = userService.getRole(username);
        //每个角色拥有默认的权限
        String rolePermission = userService.getRolePermission(username);
        //每个用户可以设置新的权限
        String permission = userService.getPermission(username);
        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        //需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
        roleSet.add(role);
        permissionSet.add(rolePermission);
        permissionSet.add(permission);
        //设置该用户拥有的角色和权限
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);*/
        return info;
    }

    //清除缓存(例如:权限的缓存)
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}
