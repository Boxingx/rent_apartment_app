package com.example.product_module.repository;


import com.example.product_module.entity.BookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistoryEntity, Long> {

    List<BookingHistoryEntity> getBookingHistoryEntitiesBySchedulerMailReviewEquals (String s);

}
