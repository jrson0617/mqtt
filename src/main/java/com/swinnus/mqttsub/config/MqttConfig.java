package com.swinnus.mqttsub.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * MQTT 설정을 위한 Configuration 클래스
 * 
 * @author jrson
 * @since 2025.01.26
 */
@Configuration
public class MqttConfig {
    
    @Value("${mqtt.broker.url}")
    private String brokerUrl;
    
    @Value("${mqtt.client.id}")
    private String clientId;
    
    @Value("${mqtt.topic}")
    private String topic;
    
    /**
     * MQTT 클라이언트 팩토리를 생성하는 Bean
     * MQTT 브로커 연결 설정을 담당
     * 
     * @return MqttPahoClientFactory MQTT 클라이언트 팩토리 인스턴스
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        
        options.setServerURIs(new String[] { brokerUrl });
        options.setCleanSession(true);
        
        factory.setConnectionOptions(options);
        return factory;
    }
    
    /**
     * MQTT 메시지를 처리할 채널을 생성하는 Bean
     * 
     * @return MessageChannel MQTT 입력 채널
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }
    
    /**
     * MQTT 메시지 수신을 위한 어댑터를 생성하는 Bean
     * 지정된 토픽으로부터 메시지를 수신하여 입력 채널로 전달
     * 
     * @return MqttPahoMessageDrivenChannelAdapter MQTT 메시지 수신 어댑터
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter(
                clientId + "_" + System.currentTimeMillis(),  // 유니크한 클라이언트 ID
                mqttClientFactory(), 
                topic
            );
        
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        
        return adapter;
    }
} 