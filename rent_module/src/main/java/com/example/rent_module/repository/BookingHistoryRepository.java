package com.example.rent_module.repository;


import com.example.rent_module.model.entity.BookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


//todo нет аннотации репозиторий почему всё работает? читать про аннотации
public interface BookingHistoryRepository extends JpaRepository<BookingHistoryEntity, Long> {

    List<BookingHistoryEntity> getBookingHistoryEntitiesBySchedulerProcessingEquals(String s);

    List<BookingHistoryEntity> getBookingHistoryEntitiesByStartDateBetween (LocalDate startDate, LocalDate endDate);


}
