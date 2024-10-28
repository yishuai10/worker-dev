package com.xiaoqiu.mq;

import com.rabbitmq.client.Channel;
import com.xiaoqiu.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信监听消费者
 * @author xiaoqiu
 */
@Slf4j
@Component
public class RabbitMqSmsConsumer {

    @Autowired
    private SMSUtils smsUtils;

    /**
     * 监听发送短信队列，并且处理消息
     */
//    @RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
//    public void watchQueue(String payload, Message message) {
//
//        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//        log.info("routingKey = " + routingKey);
//
//        String msg = payload;
//        log.info("msg = " + msg);
//
//        if (routingKey.equalsIgnoreCase(RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
//            // 此处为短信发送的消息消费处理
//            SmsContentQo contentQo = JSONUtil.toBean(msg, SmsContentQo.class);
//            smsUtils.sendSMS(contentQo.getMobile(), contentQo.getContent());
//        }
//    }

    /**
     * 手动ACK机制确认消息
     */
    @RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue(Message message, Channel channel) throws Exception {

        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.info("routingKey = " + routingKey);
            String msg = new String(message.getBody());
            log.info("msg = " + msg);

            /*
                  deliveryTag: 消息投递的标签
                  multiple: 批量确认所有消费者获得的消息
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                            true);
        } catch (Exception e) {
            log.error("消费短信队列异常", e);
            /*
                requeue: true：重回队列 false：丢弃消息
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    true,
                    false);
//            channel.basicReject();
        }

    }
}
