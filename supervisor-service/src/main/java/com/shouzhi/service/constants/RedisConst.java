package com.shouzhi.service.constants;

import com.shouzhi.basic.utils.CredentialsUtil;

/**
 * Redis相关常量
 * @author WX
 * @date 2020-06-28 10:05:12
 */
public final class RedisConst {

    /**
     * 失效时间900S,15分钟
     */
    public static final int TTL_SECONDS_900 = 900;
    /**
     * 失效时间300S,5分钟
     */
    public static final int TTL_SECONDS_300 = 300;

    /**
     * 私有化构造方法
     */
    private RedisConst(){}

    /**
     * 用户信息
     */
    public static final String INFO_USER_ = "info_user_";
    /**
     * 邮箱验证码
     */
    public static final String EMAIL_VALID_CODE_ = "email_valid_code_";
    /**
     * 手机验证码
     */
    public static final String MOBILE_VALID_CODE_ = "mobile_valid_code_";
    /**
     * 修改密码密钥
     */
    public static final String UPDATE_PWD_KEY_ = "update_pwd_key_";
    /**
     * 修改手机密钥
     */
    public static final String UPDATE_MOBILE_KEY_ = "update_mobile_key_";
    /**
     * 修改邮箱密钥
     */
    public static final String UPDATE_EMAIL_KEY_ = "update_email_key_";
    /**
     * 修改密保问题密钥
     */
    public static final String UPDATE_SECURITY_KEY_ = "update_security_key_";
    /**
     * 注销用户步骤一密钥
     */
    public static final String CANCEL_ACCOUNT_1_KEY_ = "cancel_account_1_key_";
    /**
     * 注销用户步骤二密钥
     */
    public static final String CANCEL_ACCOUNT_2_KEY_ = "cancel_account_2_key_";


    public static String INFO_USER(String userName){
        return INFO_USER_ + CredentialsUtil.MD5Pwd(userName, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String EMAIL_VALID_CODE(String email){
        return EMAIL_VALID_CODE_ + CredentialsUtil.MD5Pwd(email, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String MOBILE_VALID_CODE(String mobile){
        return MOBILE_VALID_CODE_ + CredentialsUtil.MD5Pwd(mobile, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String UPDATE_PWD_KEY(String rowId){
        return UPDATE_PWD_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String UPDATE_MOBILE_KEY(String rowId){
        return UPDATE_MOBILE_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String UPDATE_EMAIL_KEY(String rowId){
        return UPDATE_EMAIL_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    public static String UPDATE_SECURITY_KEY(String rowId){
        return UPDATE_SECURITY_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

    /**
     * 注销账户步骤一key
     */
    public static String CANCEL_ACCOUNT_1_KEY(String rowId){
        return CANCEL_ACCOUNT_1_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }
    /**
     * 注销账户步骤二key
     */
    public static String CANCEL_ACCOUNT_2_KEY(String rowId){
        return CANCEL_ACCOUNT_2_KEY_ + CredentialsUtil.MD5Pwd(rowId, SecretKeyConst.MD5_SALT, SecretKeyConst.MD5_HASH_ITERATIONS);
    }

}
