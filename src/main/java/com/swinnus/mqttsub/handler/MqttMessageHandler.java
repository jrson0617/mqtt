package com.swinnus.mqttsub.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.swinnus.mqttsub.dto.CoaMessage;
import java.util.List;
import com.swinnus.mqttsub.service.ContainerDataService;
import lombok.RequiredArgsConstructor;
import javax.annotation.PostConstruct;

/**
 * MQTT 메시지 처리를 위한 핸들러 클래스
 * 
 * @author jrson
 * @since 2025.01.26
 */
@Slf4j
@Component
@RequiredArgsConstructor  // 이 어노테이션이 final 필드에 대한 생성자를 자동으로 생성
public class MqttMessageHandler {

    private final ObjectMapper objectMapper;
    private final ContainerDataService containerDataService;

    @PostConstruct
    public void init() {
        log.info("MqttMessageHandler initialized");
    }

    /**
     * MQTT 메시지를 수신하여 처리하는 메서드
     * JSON 형식의 메시지를 파싱하여 처리
     *
     * @param message 수신된 MQTT 메시지
     */
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        log.info("Message received on topic: {}", 
            message.getHeaders().get("mqtt_receivedTopic"));  // 토픽 정보 로깅
        
        try {
            String payload = message.getPayload().toString();
            log.debug("Received raw message: {}", payload);  // 전체 메시지 디버그 로깅

            try {
                // JSON 배열을 CoaMessage 리스트로 변환
                List<CoaMessage> coaMessages = objectMapper.readValue(
                    payload, 
                    new TypeReference<List<CoaMessage>>() {}
                );
                
                // 각 메시지 처리
                for (CoaMessage coaMessage : coaMessages) {
                    processCoaMessage(coaMessage);
                    containerDataService.saveCoaMessage(coaMessage);  // DB 저장 추가
                }
            } catch (Exception e) {
                log.error("JSON parsing error: {}", e.getMessage());
                log.debug("Problematic payload: {}", payload);
            }
            
        } catch (Exception e) {
            log.error("Message processing error: {}", e.getMessage(), e);
        }
    }

    /**
     * COA 메시지 처리
     */
    private void processCoaMessage(CoaMessage message) {
        log.info("Processing COA message for container: {}", message.getContainerId());
        
        // 온도 관련 정보 로깅
        log.info("Temperature - Setpoint: {}°C, Supply: {}°C, Return: {}°C, Ambient: {}°C",
            message.getSetpoint(), message.getSupply(), 
            message.getReturnTemp(), message.getAmbient());
        
        // 알람 정보 처리
        if (message.getAlarms() != null && !message.getAlarms().isEmpty()) {
            log.warn("Active alarms for container {}: {}", 
                message.getContainerId(), message.getAlarms());
        }
        
        // 위치 정보 처리
        if (message.getLatitude() != null && message.getLongitude() != null) {
            log.info("Container location: {}, {}", 
                message.getLatitude(), message.getLongitude());
        }
    }
} 