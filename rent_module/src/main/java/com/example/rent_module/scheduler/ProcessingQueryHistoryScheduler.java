package com.example.rent_module.scheduler;

import com.example.rent_module.model.entity.BookingHistoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.rent_module.constant_project.ConstantProject.HISTORY_QUERY;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProcessingQueryHistoryScheduler {

    private final JdbcTemplate jdbcTemplate;
    @Scheduled(fixedRate = 10000)
    public void startProcessingQueryScheduler() {
//        jdbcTemplate.execute(HISTORY_QUERY, );
        log.info("шедулер начал свою работу " + LocalDateTime.now());
    }
}
