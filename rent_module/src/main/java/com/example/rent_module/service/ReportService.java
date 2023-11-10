package com.example.rent_module.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface ReportService {
    String getReport(String year, Integer mouth);
}
