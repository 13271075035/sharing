package com.sharing.RabbitMQ;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.sharing.entity.SysFire;
import com.sharing.entity.SysRoom;
import com.sharing.service.impl.SysFireServiceImpl;
import com.sharing.service.impl.SysRoomServiceImpl;
import com.sharing.util.WebSocketServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@IntegrationComponentScan
@Slf4j
public class MqttReceiveConfig {

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout;   //连接超时

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private WebSocketServer socket;

    @Autowired
    private SysFireServiceImpl impl;

    @Autowired
    private SysRoomServiceImpl roomImpl;

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "_inbound", mqttClientFactory(),
                        defaultTopic);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @SneakyThrows
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                log.info("主题：{}，消息接收到的数据：{}", message.getHeaders().get("mqtt_receivedTopic"), message.getPayload());
                if(message.getPayload().equals("$APP,FIREON*")){
                    List<SysFire> list = impl.list();
                    if(list.size()!=0) {


                        if (list.get(list.size() - 1).getSysState() == 2) {
                            QueryWrapper<SysRoom> query = new QueryWrapper<SysRoom>();
                            query.lambda().eq(SysRoom::getSpareFirst, message.getHeaders().get("mqtt_receivedTopic"));
                            List<SysRoom> roomList = roomImpl.list(query);
                            SysFire fire = new SysFire();
                            fire.setSysDeviceAddress(roomList.get(0).getSysRoomname());
                            fire.setSysEquipmentName((String) message.getHeaders().get("mqtt_receivedTopic"));
                            fire.setSysState(0);
                            impl.save(fire);
                            Map map = new HashMap();
                            map.put("type","warning");
                            map.put("data",JSON.toJSONString(fire));
                            socket.BroadCastInfo(new Gson().toJson(map));

                        } else if (list.get(list.size() - 1).getSysState() == 1) {


                        }
                    }else{
                        QueryWrapper<SysRoom> query = new QueryWrapper<SysRoom>();
                        query.lambda().eq(SysRoom::getSpareFirst, message.getHeaders().get("mqtt_receivedTopic"));
                        List<SysRoom> roomList = roomImpl.list(query);
                        SysFire fire = new SysFire();
                        fire.setSysDeviceAddress(roomList.get(0).getSysRoomname());
                        fire.setSysEquipmentName((String) message.getHeaders().get("mqtt_receivedTopic"));
                        fire.setSysState(0);
                        impl.save(fire);
                        socket.BroadCastInfo(JSON.toJSONString(fire));
                    }

                }else if(message.getPayload().equals("$APP,FIREOFF*")){
                    List<SysFire> list = impl.list();
                    SysFire fire = new SysFire();
                    fire.setSysFire(list.get(list.size()-1).getSysFire());
                    fire.setSysState(2);
                    impl.updateById(fire);
                    socket.BroadCastInfo("解除");

                }

            }
        };
    }
}
