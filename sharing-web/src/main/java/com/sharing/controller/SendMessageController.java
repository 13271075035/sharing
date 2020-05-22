package com.sharing.controller;

import com.sharing.RabbitMQ.MqttGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description :
 **/
@RestController
@RequestMapping("/rabbit")
public class SendMessageController {

    @Autowired
    private MqttGateway mqttGateway;

    @RequestMapping("/sendMqttUnlock")
    public String sendMqttUnlock(){
        mqttGateway.sendToMqtt("$APP,UNLOCK*","865373048271337APP");
        return "OK";
    }

    @RequestMapping("/sendMqttLastfuve")
    public String sendMqttLastfuve(){
        mqttGateway.sendToMqtt("$APP,LASTFIVE*","865373048271337APP");
        return "OK";
    }

    @RequestMapping("/sendMqttLock")
    public String sendMqttLock(){
        mqttGateway.sendToMqtt("$APP,LOCK*","865373048271337APP");
        return "OK";
    }
    @RequestMapping("/sendMqttTakeover")
    public String sendMqttTakeover(){
        mqttGateway.sendToMqtt("$APP,TAKEOVER*  ","865373048271337APP");
        return "OK";
    }

}


