package com.swinnus.mqttsub.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "container_data")
public class ContainerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String containerId;
    private LocalDateTime timestamp;
    private Float setpoint;
    private Float supply;
    private Float returnTemp;
    private Float ambient;
    private Float relativeHumidity;
    private String controllerModel;
    private Float power;
    private Double latitude;
    private Double longitude;
    
    @Column(length = 1000)
    private String alarmInfo;
} 