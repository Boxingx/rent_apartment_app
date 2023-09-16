package com.example.product_module.repository;


import com.example.product_module.entity.BookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingHistoryRepository extends JpaRepository<BookingHistoryEntity, Long> {
}
