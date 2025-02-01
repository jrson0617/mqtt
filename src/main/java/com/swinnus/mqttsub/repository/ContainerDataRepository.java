package com.swinnus.mqttsub.repository;

import com.swinnus.mqttsub.entity.ContainerData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ContainerDataRepository extends JpaRepository<ContainerData, Long> {
    // 컨테이너 ID로 데이터 찾기
    List<ContainerData> findByContainerId(String containerId);
    
    // 특정 기간 동안의 데이터 찾기
    List<ContainerData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
} 