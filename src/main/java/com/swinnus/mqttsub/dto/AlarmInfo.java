package com.swinnus.mqttsub.dto;

import lombok.Data;

/**
 * 알람 정보를 위한 DTO 클래스
 * 
 * @author jrson
 * @since 2025.01.26
 */
@Data
public class AlarmInfo {
    private String alarm_name;    // 알람 코드 (예: AL026)
    private String alarm_desc;    // 알람 설명
} 