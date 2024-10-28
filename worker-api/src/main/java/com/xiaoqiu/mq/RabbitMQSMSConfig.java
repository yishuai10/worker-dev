package com.xiaoqiu.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 的配置类
 * @author xiaoqiu
 */
@Configuration
public class RabbitMQSMSConfig {

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
        return QueueBuilder
                .durable(SMS_QUEUE)
                .withArgument("x-message-ttl", 30*1000)
                .withArgument("x-dead-letter-exchange",
                        RabbitMQSMSConfig_Dead.SMS_EXCHANGE_DEAD)
                .withArgument("x-dead-letter-routing-key",
                        RabbitMQSMSConfig_Dead.ROUTING_KEY_SMS_DEAD)
                .withArgument("x-max-length", 6)
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
