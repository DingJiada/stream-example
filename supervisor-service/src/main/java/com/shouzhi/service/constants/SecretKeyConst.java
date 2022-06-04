package com.shouzhi.service.constants;

/**
 * 密钥常量，用于存储各种密钥常量
 * @author WX
 * @date 2021-01-04 17:04:43
 */
public class SecretKeyConst {
    /**
     * 私有化构造方法
     */
    private SecretKeyConst(){}

    /**
     * MD5盐值
     */
    public static final String MD5_SALT= "salt";
    /**
     * MD5哈希迭代数
     */
    public static final int MD5_HASH_ITERATIONS= 1024;
}
