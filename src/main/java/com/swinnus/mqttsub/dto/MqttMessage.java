package com.swinnus.mqttsub.dto;

import lombok.Data;

@Data
public class MqttMessage {
    private String deviceId;
    private String type;
    private Object data;
    private String timestamp;
} 