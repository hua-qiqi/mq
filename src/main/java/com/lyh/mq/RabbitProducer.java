package com.lyh.mq;




import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class RabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("directSend")
    public void directSend() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("YYYY-mm-DD hh:MM:ss").format(date);
        System.out.println("[string] send msg:" + dateString);
        // 第一个参数为刚刚定义的队列名称
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("basicExchange","basicKey",dateString,correlationData);
    }

    @RequestMapping("fanoutSend")
    public void fanoutSend() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("YYYY-mm-DD hh:MM:ss").format(date);
        System.out.println("[string] send msg:" + dateString);
        // 第一个参数为刚刚定义的队列名称
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("fanoutExchange","",dateString,correlationData);
    }


    @RequestMapping("sendTopic")
    public void sendTopic() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("YYYY-mm-DD hh:MM:ss").format(date);
        System.out.println("[string] send msg:" + dateString);
        // 第一个参数为刚刚定义的队列名称
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend(MqConfig.TOPIC_EXCHANGE, "lzc.message", dateString );
        this.rabbitTemplate.convertAndSend(MqConfig.TOPIC_EXCHANGE, "lzc.lzc", dateString);

    }
}
