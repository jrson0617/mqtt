package com.swinnus.mqttsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

/**
 * MQTT Subscriber 애플리케이션의 메인 클래스
 * 
 * @author jrson
 * @since 2025.01.26
 */
@SpringBootApplication
@IntegrationComponentScan
public class MqttSubApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqttSubApplication.class, args);
    }
} 