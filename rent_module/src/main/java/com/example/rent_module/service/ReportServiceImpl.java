package com.example.rent_module.service;

import com.example.rent_module.model.entity.BookingHistoryEntity;
import com.example.rent_module.repository.BookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final BookingHistoryRepository bookingHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookingHistoryRepository.class);

    @Override
    public String getReport(String year, Integer month) {

        LocalDate startDate = prepareDate(year, month, null);
        List<BookingHistoryEntity> bookingHistoryEntities = bookingHistoryRepository.getBookingHistoryEntitiesByStartDateBetween(startDate,
                prepareDate(null, null, startDate));
        report(bookingHistoryEntities);
        return " ";
    }

    private void report(List<BookingHistoryEntity> bookingHistoryEntities) {

        logger.info("Класс ReportServiceImpl метод report начал выгрузку отчета");
        File template = new File("C:\\Users\\Alex\\IdeaProjects\\rent_apartment_app\\report_template.xlsx");
        try(FileInputStream fileInputStream = new FileInputStream(template);
            XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
            XSSFSheet sheet = book.getSheetAt(0);
            int rowNumber = 1;
            for(BookingHistoryEntity bH : bookingHistoryEntities) {
                XSSFRow row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(bH.getStartDate() + " по " + bH.getEndDate());
                row.createCell(1).setCellValue(bH.getDaysCount());
                row.createCell(2).setCellValue(bH.getFinalPayment());
                row.createCell(3).setCellValue(bH.getPromoCode());
                row.createCell(4).setCellValue(bH.getApartmentEntity().getAddressEntity().getCity() + " " +
                        bH.getApartmentEntity().getAddressEntity().getStreet() + " " + bH.getApartmentEntity().getAddressEntity().getBuildingNumber());
                row.createCell(5).setCellValue(bH.getClientApplicationEntity().getNickName());
                if (!isNull(bH.getProductEntity())) {
                    row.createCell(6).setCellValue(bH.getProductEntity().getDiscount());
                }
                row.createCell(7).setCellValue(" ");
                row.createCell(8).setCellValue(bH.getApartmentEntity().getAverageRating());
                row.createCell(9).setCellValue(" ");
            }
            FileOutputStream fileOutputStream = new FileOutputStream(template);
            book.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            logger.info("Класс ReportServiceImpl метод report - отчет выгружен");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Класс ReportServiceImpl метод report - ошибка выгрузки отчета");
        }
    }

    private LocalDate prepareDate(String year, Integer month, LocalDate date) {

        try {
            if (nonNull(date)) {
                return date.withDayOfMonth(date.lengthOfMonth());
            }
            return LocalDate.of(Integer.parseInt(year), month, 1);
        } catch (NumberFormatException ex) {
            logger.error("Класс ReportServiceImpl метод prepareDate");
            throw new NumberFormatException(ex.getMessage());
        }
    }
}
