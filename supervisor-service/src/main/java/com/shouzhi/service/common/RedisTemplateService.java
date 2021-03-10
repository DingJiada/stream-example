package com.shouzhi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisTemplate测试类，没有实现接口，直接编写测试类
 * @author WX
 * @date 2019-08-22 21:18:03
 */
@Service("redisTemplateService")
@Transactional  //该类下所有的方法都受事务控制
public class RedisTemplateService {

    //注入StringRedisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 切换redis数据库
     * @param index database index
     */
    public RedisTemplateService selectDB(int index){
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory)redisTemplate.getConnectionFactory();
        if(lettuceConnectionFactory!=null){
            lettuceConnectionFactory.setDatabase(index);
            redisTemplate.setConnectionFactory(lettuceConnectionFactory);
            lettuceConnectionFactory.resetConnection();
        }
        return this;
    }

    /*
     * springboot默认提供了两个RedisTemplate: StringRedisTemplate、RedisTemplate, 用于操作Redis, 封装了很多API
     * StringRedisTemplate继承自RedisTemplate, 只能操作字符串, 在配置好application.properties文件后可以直接使用
     * RedisTemplate默认是RedisTemplate<Object,Object>,可以操作另外4中类型,但为了方便,不每次都转换类型,一般都重新配置RedisTemplate<String,Object>
     * @param key 
     * @author WX
     * @date 2019-08-31 17:49:16
     */
    public String getStr(String key) {
        // RedisTemplate封装了5个 opsForHash、opsForList、opsForSet、opsForZSet、opsForValue 用于操作集合及字符串
        return stringRedisTemplate.opsForValue().get(key);
    }

    public List<String> getMultiStr(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    public void setStr(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setStr(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void Hput(String key, Object hashKey, Object value) {
        Hput(key, hashKey, value, null);
    }

    public void Hput(String key, Object hashKey, Object value, Long timeout) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        if(timeout!=null){
            expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public void HputAll(String key, Map<String,Object> map) {
        HputAll(key, map, null);
    }

    public void HputAll(String key, Map<String,Object> map, Long timeout) {
        redisTemplate.opsForHash().putAll(key, map);
        if(timeout!=null){
            expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public Object Hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object,Object> Hentries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    public void expire(String key, Long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean deletePattern(String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        return this.deleteCollection(keys);
    }

    public Boolean deleteCollection(Collection<String> keys){
        if(keys.size()>0){
            Long delete = redisTemplate.delete(keys);
            if(delete==keys.size()){
                return true;
            }
        }
        return false;
    }

}
