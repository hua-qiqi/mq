package com.lyh.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MqConfig {

    private static Logger log = LoggerFactory.getLogger(MqConfig.class);

    @Resource
    RabbitTemplate rabbitTemplate;


    @Bean
    public AmqpTemplate amqpTemplate() {
//          使用jackson 消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");
//        开启returncallback     yml 需要 配置    publisher-returns: true
        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConnectionFactory(new CachingConnectionFactory());
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();

            log.error("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });

        //        消息确认  yml 需要配置   publisher-returns: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.error("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }

    //TODO：基本消息模型构建
    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange("basicExchange", true,false);
    }

    @Bean(name = "basicQueue")
    public Queue basicQueue(){
        return new Queue("basicQueue", true);
    }

    @Bean
    public Binding basicBinding(){
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with("basicKey");
    }



    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange",true,false);
    }

    @Bean("fanout01")
    public Queue fanout01(){
        return QueueBuilder.durable("fanout01").build();
    }

    @Bean("fanout02")
    public Queue fanout02(){
        return QueueBuilder.durable("fanout02").build();
    }

    @Bean
    public Binding fanoutBind01(){
        return BindingBuilder.bind(fanout01()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBind012(){
        return BindingBuilder.bind(fanout02()).to(fanoutExchange());
    }

    /**
     * Topic模式
     * @return
     */
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";

    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2);
    }
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message");
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lzc.sdasa");
    }






}
