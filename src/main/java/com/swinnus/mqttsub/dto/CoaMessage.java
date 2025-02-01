package com.swinnus.mqttsub.dto;

import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swinnus.mqttsub.dto.AlarmInfo;

/**
 * COA Push API 메시지를 위한 DTO 클래스
 * 
 * @author jrson
 * @since 2025.01.26
 */
@Data
public class CoaMessage {
    @JsonProperty("p1")
    private String containerId;           // Container ID
    
    @JsonProperty("p2")
    private Integer sourcePower;          // Source Power status
    
    @JsonProperty("p3")
    private Float setpoint;               // Temperature setpoint (°C)
    
    @JsonProperty("p4")
    private Float supply;                 // Supply temperature (°C)
    
    @JsonProperty("p5")
    private Float returnTemp;             // Return temperature (°C)
    
    @JsonProperty("p6")
    private Float ambient;                // Ambient temperature (°C)
    
    @JsonProperty("p7")
    private Float relativeHumidity;       // Relative Humidity (%)
    
    @JsonProperty("p14")
    private String controllerModel;       // Controller Model
    
    @JsonProperty("p15")
    private String controllerTime;        // Controller time
    
    @JsonProperty("p100")
    private Float humiditySetpoint;       // Humidity setpoint (%)
    
    @JsonProperty("p105")
    private String controllerSerialNo;    // Controller serial number
    
    @JsonProperty("p106")
    private String sourceSW;              // Source software version
    
    @JsonProperty("C100")
    private String unitModelNumber;       // Unit Model Number
    
    @JsonProperty("C002")
    private Integer compressorHM;         // Compressor running hours
    
    @JsonProperty("C013")
    private Integer minutesToDefrost;     // Minutes to defrost
    
    @JsonProperty("C015")
    private List<String> criticalAlarms;  // Critical alarms
    
    @JsonProperty("C016")
    private List<String> activeAlarms;   // Active alarms - 정수 배열을 문자열 배열로 변경
    
    @JsonProperty("C022")
    private Float power;                  // Power consumption (kW)
    
    @JsonProperty("C023")
    private Integer cumulativePower;      // Cumulative power (kW*hr)
    
    @JsonProperty("lat")
    private Double latitude;              // Latitude
    
    @JsonProperty("long")
    private Double longitude;             // Longitude
    
    @JsonProperty("Alarms")
    private List<AlarmInfo> alarms;       // Detailed alarm information
} 