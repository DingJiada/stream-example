package com.shouzhi.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitmqConfig
 * @author WX
 * @date 2019-12-22 20:52:57
 */
@Configuration
public class RabbitmqConfig {

    // 声明队列
    public static final String QUEUE_MEDIA_SERVER_DVR = "queue_media_server_dvr";
    // 声明交换机
    public static final String EXC_MEDIA_PROCESSOR = "exc_media_processor";
    // 声明routingKey
    public static final String ROUTINGKEY_MEDIA_SERVER_DVR = "routingkey_media_server_dvr";

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 32;

    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }


    // 声明交换机,bean名称使用常量会被字符串代替,方法名称无所谓,这里直接以常量命名了
    @Bean(EXC_MEDIA_PROCESSOR)
    public Exchange EXC_MEDIA_PROCESSOR(){
        // ExchangeBuilder可以声明多个交换机 如ExchangeBuilder.directExchange()等
        // durable(true) 表示交换机是否持久化,将来mq重启后交换机仍存在
        return ExchangeBuilder.directExchange(EXC_MEDIA_PROCESSOR).durable(true).build();
    }


    // 声明队列-流媒体服务器DVR处理
    @Bean(QUEUE_MEDIA_SERVER_DVR)
    public Queue QUEUE_MEDIA_SERVER_DVR(){
        return new Queue(QUEUE_MEDIA_SERVER_DVR,true,false,false);
    }


    // 绑定队列到交换机
    @Bean
    public Binding BINDING_QUEUE_MEDIA_SERVER_DVR(@Qualifier(QUEUE_MEDIA_SERVER_DVR) Queue queue,
                                                        @Qualifier(EXC_MEDIA_PROCESSOR) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_MEDIA_SERVER_DVR).noargs();
    }
}
