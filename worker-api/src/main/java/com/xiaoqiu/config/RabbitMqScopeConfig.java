package com.xiaoqiu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiaoqiu
 */
@Slf4j
@Configuration
public class RabbitMqScopeConfig implements WebMvcConfigurer {

    @Bean(name = "xiaoqiuRabbitTemplate")
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //成功回调
        template.setConfirmCallback(new Callback());
        // 开启mandatory模式（开启失败回调）
        template.setMandatory(true);
        //失败回调
        template.setReturnsCallback(new Callback());
        return template;
    }

    private static class Callback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
        // 消息可靠性投递-定义confirm回调（生产者 - exchange之间的错误）
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (correlationData != null) {
                log.info("进入confirm， correlationData：{}", correlationData.getId());
            }

            if (ack) {
                log.info("交换机成功接收到消息！");
            } else {
                log.info("交换机接收消息失败！失败原因： {}", cause);
            }
        }

        // 消息可靠性投递- 定义return回调（exchange - routing_key之间的错误）
        @Override
        public void returnedMessage(ReturnedMessage returned) {
            log.info("进入return");
            log.info(returned.toString());
        }
    }
}
