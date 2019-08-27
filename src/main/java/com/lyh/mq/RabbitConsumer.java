package com.lyh.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {
    private static final Logger log= LoggerFactory.getLogger(RabbitConsumer.class);

    /**
     * 监听消费消息
     * @param message
     */
    @RabbitListener(queues ="basicQueue")
    public void consumeMessage(@Payload byte[] message){
        try {
            //TODO：接收String
            String result=new String(message,"UTF-8");
            log.info("接收String消息： {} ",result);
        }catch (Exception e){
            log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
        }
    }


    /**
     * 监听消费消息
     * @param message
     */
    @RabbitListener(queues ="fanout01")
    public void consumeMessage1(@Payload byte[] message){
        try {
            //TODO：接收String
            String result=new String(message,"UTF-8");
            log.info("接收String1消息： {} ",result);
        }catch (Exception e){
            log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
        }
    }

    /**
     * 监听消费消息
     * @param message
     */
    @RabbitListener(queues ="fanout02")
    public void consumeMessage2(@Payload byte[] message){
        try {
            //TODO：接收String
            String result=new String(message,"UTF-8");
            log.info("接收String2消息： {} ",result);
        }catch (Exception e){
            log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
        }
    }
    @RabbitListener(queues = MqConfig.TOPIC_QUEUE1)
    public void receiveTopic1(@Payload byte[] message) {
        log.info("【receiveTopic1监听到消息】" + message.toString());
    }
    @RabbitListener(queues = MqConfig.TOPIC_QUEUE2)
    public void receiveTopic2(@Payload byte[] message) {
        log.info("【receiveTopic2监听到消息】" + message.toString());
    }



}
