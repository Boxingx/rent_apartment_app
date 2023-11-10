package com.example.rent_module.service;

import com.example.rent_module.repository.BookingHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReportServiceImpl implements ReportService {

    private BookingHistoryRepository bookingHistoryRepository;

    @Override
    public String getReport(String year, Integer month) {
        LocalDate localDate = prepareDate(year, month);
        bookingHistoryRepository.getBookingHistoryEntitiesByStartDateMonth(localDate.getMonth());
        return null;
    }

    private LocalDate prepareDate(String year, Integer mouth) {
        try {
            return LocalDate.of(Integer.parseInt(year), mouth, 1);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(ex.getMessage());
        }

    }
}
