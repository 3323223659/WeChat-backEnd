package org.itzixi.controller;

import jakarta.annotation.Resource;
import org.itzixi.pojo.netty.ChatMsg;
import org.itzixi.rabbitmq.RabbitMQTestConfig;
import org.itzixi.utils.JsonUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther yang
 */
@RestController
@RequestMapping("m")
public class HelloController {

    // 127.0.0.1:88/a/hello

    @GetMapping("hello")
    public Object hello() {
        return "Hello world~";
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("mq")
    public Object mq() {

        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setMsg("hello world~~~");
        String msg = JsonUtils.objectToJson(chatMsg);

        rabbitTemplate.convertAndSend(
                RabbitMQTestConfig.TEST_EXCHANGE,
                RabbitMQTestConfig.ROUTING_KEY_TEST_SEND,
                msg);

        return "ok";
    }

}
