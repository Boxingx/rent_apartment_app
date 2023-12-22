package com.example.rent_module.scheduler;

import com.example.rent_module.model.entity.BookingHistoryEntity;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.repository.ApartmentRepository;
import com.example.rent_module.repository.BookingHistoryRepository;
import com.example.rent_module.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.rent_module.constant_project.ConstantProject.TRUE;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProcessingQueryHistoryScheduler {

    private final ApartmentRepository apartmentRepository;

    private final BookingHistoryRepository bookingHistoryRepository;

    private final ClientRepository clientRepository;

    public static DateTimeFormatter authFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
    /**
     * Метод с шедулером который переодически проверяет активные бронирования, если бронирование уже закончилось он сделает квартиру снова свободной,
     * а так же само бронирование будет закончено.
     */
    @Scheduled(fixedDelay = 35_000)
    public void startProcessingQueryScheduler() {
//        log.info("Квартирный шедулер начал свою работу " + LocalDateTime.now());

        List<BookingHistoryEntity> bookingHistoryEntities = bookingHistoryRepository.getBookingHistoryEntitiesBySchedulerProcessingEquals("false");
        for (BookingHistoryEntity e : bookingHistoryEntities) {
            if (e.getEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                e.getApartmentEntity().setStatus(TRUE);
                apartmentRepository.save(e.getApartmentEntity());
                e.setSchedulerProcessing(TRUE);
                bookingHistoryRepository.save(e);

                //TODO может добавить в шедулер высчитывание среднего рейтинга по квартирам
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkTokenScheduler() {
//        log.info("Токен шедулер начал свою работу " + LocalDateTime.now());
        List<ClientApplicationEntity> clientEntities = clientRepository.findClientApplicationEntitiesByUserTokenNotNull();
        for (ClientApplicationEntity c : clientEntities) {
            if (parseTokenValue(c.getUserToken()).isBefore(LocalDateTime.now())) {
                c.setUserToken(null);
                clientRepository.save(c);
            }
        }
    }

    private LocalDateTime parseTokenValue(String token) {
        int index = token.indexOf("|") + 1;
        String timeValue = token.substring(index);
        return LocalDateTime.parse(timeValue, authFormatter);
    }
}
