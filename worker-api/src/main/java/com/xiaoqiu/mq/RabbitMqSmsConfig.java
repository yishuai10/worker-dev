package com.xiaoqiu.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 的配置类
 * @author xiaoqiu
 */
@Slf4j
@Configuration
public class RabbitMqSmsConfig {

    /**
     * 定义交换机的名称
     */
    public static final String SMS_EXCHANGE = "sms_exchange";

    /**
     * 定义队列的名称
     */
    public static final String SMS_QUEUE = "sms_queue";

    /**
     * 定义路由key
     */
    public static final String ROUTING_KEY_SMS_SEND_LOGIN = "xiaoqiu.sms.send.login";

    @Bean(SMS_EXCHANGE)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(SMS_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean(SMS_QUEUE)
    public Queue queue() {
        log.info("创建短信队列");
        return QueueBuilder
                .durable(SMS_QUEUE)
                // 队列超时时间，可以和消息队列超时时间共用
                .withArgument("x-message-ttl", 30 * 1000)
                // 队列最大长度
                .withArgument("x-max-length", 6)
                // 死信队列交换机
                .withArgument("x-dead-letter-exchange",
                        RabbitMqSmsConfigDead.SMS_EXCHANGE_DEAD)
                // 死信队列路由key
                .withArgument("x-dead-letter-routing-key",
                        RabbitMqSmsConfigDead.ROUTING_KEY_SMS_DEAD)
                .build();
    }

    @Bean
    public Binding smsBinding(@Qualifier(SMS_EXCHANGE) Exchange exchange,
                              @Qualifier(SMS_QUEUE) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("xiaoqiu.sms.#")
                .noargs();
    }

}
