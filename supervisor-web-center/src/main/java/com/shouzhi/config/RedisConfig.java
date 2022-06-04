package com.shouzhi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类,完成SpringBoot对Redis的整合
 *
 * @author WX
 * @date 2019-08-31 18:34:11
 */
@Configuration  //让SpringBoot启动时自动加载该类
public class RedisConfig {

    /**
     * 重新配置RedisTemplate<String, Object>, 并添对象的序列化方式
     * 默认的RedisTemplate是RedisTemplate<Object, Object>,且不能将对象直接序列化到redis数据库中
     * 1.方法名称必须为redisTemplate,原因：在RedisAutoConfiguration类中有个@ConditionalOnMissingBean(name = "redisTemplate")注解
     *   注解意思是：如果Spring容器中有RedisTemplate对象了,这个自动配置的RedisTemplate不会实例化。因此我们可以直接自己写个配置类,配置RedisTemplate。
     * 2.方法返回值必须为RedisTemplate<K, V>
     * @param redisConnectionFactory redis连接工厂,里面含有redis连接相关信息,如主机地址、密码、端口、以及连接池的对象信息等等
     * @author WX
     * @date 2019-08-31 18:39:43
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        //使用StringRedisSerializer来序列化和反序列化redis的Key
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        //value 一般不建议采用JdkSerializationRedisSerializer序列化,因为以jdk的方式序列化相比Json序列后的值要大很多,至少5倍以上,会对redis空间造成浪费
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //开启事务
        // redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
