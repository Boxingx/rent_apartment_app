package com.example.rent_module.repository;


import com.example.rent_module.model.entity.BookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface BookingHistoryRepository extends JpaRepository<BookingHistoryEntity, Long> {

    List<BookingHistoryEntity> getBookingHistoryEntitiesBySchedulerProcessingEquals(String s);

    List<BookingHistoryEntity> getBookingHistoryEntitiesByStartDateMonth(Month value);
}
