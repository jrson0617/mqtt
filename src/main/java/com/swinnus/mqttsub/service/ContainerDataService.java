package com.swinnus.mqttsub.service;

import com.swinnus.mqttsub.entity.ContainerData;
import com.swinnus.mqttsub.repository.ContainerDataRepository;
import com.swinnus.mqttsub.dto.CoaMessage;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerDataService {
    
    private final ContainerDataRepository repository;
    private final ObjectMapper objectMapper;
    
    // 배치 처리를 위한 큐
    private final ConcurrentLinkedQueue<ContainerData> dataQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int BATCH_SIZE = 100;
    
    @PostConstruct
    public void init() {
        // 주기적으로 배치 처리 실행
        scheduler.scheduleWithFixedDelay(this::processBatch, 5, 5, TimeUnit.SECONDS);
    }
    
    public void saveCoaMessage(CoaMessage message) {
        try {
            ContainerData data = convertToContainerData(message);
            dataQueue.offer(data);  // 큐에 데이터 추가
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
    
    @Transactional
    protected void processBatch() {
        List<ContainerData> batch = new ArrayList<>();
        
        // 큐에서 배치 크기만큼 데이터 가져오기
        while (batch.size() < BATCH_SIZE && !dataQueue.isEmpty()) {
            ContainerData data = dataQueue.poll();
            if (data != null) {
                batch.add(data);
            }
        }
        
        if (!batch.isEmpty()) {
            try {
                repository.saveAll(batch);
                log.info("Saved batch of {} records", batch.size());
            } catch (Exception e) {
                log.error("Error saving batch: {}", e.getMessage(), e);
            }
        }
    }
    
    private ContainerData convertToContainerData(CoaMessage message) throws Exception {
        ContainerData data = new ContainerData();
        data.setContainerId(message.getContainerId());
        data.setTimestamp(ZonedDateTime.parse(message.getControllerTime()).toLocalDateTime());
        data.setSetpoint(message.getSetpoint());
        data.setSupply(message.getSupply());
        data.setReturnTemp(message.getReturnTemp());
        data.setAmbient(message.getAmbient());
        data.setRelativeHumidity(message.getRelativeHumidity());
        data.setControllerModel(message.getControllerModel());
        data.setPower(message.getPower());
        data.setLatitude(message.getLatitude());
        data.setLongitude(message.getLongitude());
        
        if (message.getAlarms() != null && !message.getAlarms().isEmpty()) {
            data.setAlarmInfo(objectMapper.writeValueAsString(message.getAlarms()));
        }
        
        return data;
    }
} 