package com.example.rent_module.scheduler;

import com.example.rent_module.application_exceptions.ApartmentException;
import com.example.rent_module.application_exceptions.RatingException;
import com.example.rent_module.config.CityTranslationStatic;
import com.example.rent_module.model.entity.ApartmentEntity;
import com.example.rent_module.model.entity.BookingHistoryEntity;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.model.entity.RatingEntity;
import com.example.rent_module.repository.ApartmentRepository;
import com.example.rent_module.repository.BookingHistoryRepository;
import com.example.rent_module.repository.ClientRepository;
import com.example.rent_module.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.rent_module.constant_project.ConstantProject.TRUE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ProcessingQueryHistoryScheduler {

    private final static Logger logger = LoggerFactory.getLogger(ProcessingQueryHistoryScheduler.class);

    private final ApartmentRepository apartmentRepository;

    private final BookingHistoryRepository bookingHistoryRepository;

    private final ClientRepository clientRepository;

    private final RatingRepository ratingRepository;

    public static DateTimeFormatter authFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
    /**
     * Метод с шедулером который переодически проверяет активные бронирования, если бронирование уже закончилось он сделает квартиру снова свободной,
     * а так же само бронирование будет закончено.
     */
    @Scheduled(fixedDelay = 35_000)
    public void startProcessingQueryScheduler() {
//        log.info("Квартирный шедулер начал свою работу " + LocalDateTime.now());

        logger.info("Квартирный шедулер начал свою работу " + LocalDateTime.now());
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
        logger.info("Токен шедулер начал свою работу " + LocalDateTime.now());
        List<ClientApplicationEntity> clientEntities = clientRepository.findClientApplicationEntitiesByUserTokenNotNull();
        for (ClientApplicationEntity c : clientEntities) {
            if (parseTokenValue(c.getUserToken()).isBefore(LocalDateTime.now())) {
                c.setUserToken(null);
                clientRepository.save(c);
            }
        }
    }

    /**
     * Шедулер каждый час высчитыват средний рейтинг по квартирам.
     * */
    @Scheduled(fixedRate = 3_600_000)
    public void calculateAvgRatings() {

        List<RatingEntity> allRatingsEntities = ratingRepository.getAllRatings();
        if (isNull(allRatingsEntities)) {
            throw new RatingException("Не найдены квартиры для подсчета рейтингов");
        }

        Map<Long, Double> map = allRatingsEntities.stream().collect(Collectors.groupingBy(
                ratingEntity -> ratingEntity.getApartmentEntity().getId(), Collectors.averagingDouble(RatingEntity::getRating)));

        for(Map.Entry<Long, Double> entry : map.entrySet() ) {
            ApartmentEntity apartmentEntityById = apartmentRepository.getApartmentEntityById(entry.getKey());
            if (isNull(apartmentEntityById)) {
                throw new ApartmentException("Апартаметы для посчета рейтинга не найдены");
            }
            apartmentEntityById.setAverageRating(Double.toString(entry.getValue()));
        }
    }

    private LocalDateTime parseTokenValue(String token) {
        int index = token.indexOf("|") + 1;
        String timeValue = token.substring(index);
        return LocalDateTime.parse(timeValue, authFormatter);
    }
}
