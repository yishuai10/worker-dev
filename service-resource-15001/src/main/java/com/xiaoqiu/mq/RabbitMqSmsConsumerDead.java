

package com.xiaoqiu.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 死信队列监听消费者
 * @author xiaoqiu
 */
@Slf4j
@Component
public class RabbitMqSmsConsumerDead {

    @RabbitListener(queues = {RabbitMqSmsConfigDead.SMS_QUEUE_DEAD})
    public void watchQueue(Message message, Channel channel) throws Exception {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = {}", routingKey);
        String msg = new String(message.getBody());
        log.info("msg = {}", msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        true);
        log.info("死信队列结束！");
    }
}
