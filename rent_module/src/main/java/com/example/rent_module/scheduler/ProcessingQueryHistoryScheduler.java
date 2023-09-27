package com.example.rent_module.scheduler;

import com.example.rent_module.model.entity.BookingHistoryEntity;
import com.example.rent_module.repository.ApartmentRepository;
import com.example.rent_module.repository.BookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

import static com.example.rent_module.constant_project.ConstantProject.HISTORY_QUERY;
import static com.example.rent_module.constant_project.ConstantProject.TRUE;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProcessingQueryHistoryScheduler {

    private final ApartmentRepository apartmentRepository;

    private final BookingHistoryRepository bookingHistoryRepository;

    @Scheduled(fixedRate = 20000)
    public void startProcessingQueryScheduler() {
        log.info("шедулер начал свою работу " + LocalDateTime.now());

        List<BookingHistoryEntity> bookingHistoryEntities = bookingHistoryRepository.getBookingHistoryEntitiesBySchedulerProcessingEquals("false");
        for (BookingHistoryEntity e : bookingHistoryEntities) {
            if (e.getEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                e.getApartmentEntity().setStatus(TRUE);
                apartmentRepository.save(e.getApartmentEntity());
                e.setSchedulerProcessing(TRUE);
                bookingHistoryRepository.save(e);
            }
        }
    }
}
