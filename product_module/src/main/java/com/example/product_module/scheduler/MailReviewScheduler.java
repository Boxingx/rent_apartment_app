package com.example.product_module.scheduler;

import com.example.product_module.email_sender.MailSender;
import com.example.product_module.entity.BookingHistoryEntity;
import com.example.product_module.repository.BookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service

@EnableScheduling
@RequiredArgsConstructor
public class MailReviewScheduler {

    private final BookingHistoryRepository bookingHistoryRepository;

    private final MailSender mailSender;

    @Scheduled(fixedDelay = 35_000)
    public void sendMailAfterBooking() {
        List<BookingHistoryEntity> bookingHistoryEntities = bookingHistoryRepository.getBookingHistoryEntitiesBySchedulerMailReviewEquals("false");
        for(BookingHistoryEntity e : bookingHistoryEntities) {
            if (e.getEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {

                String loginMail = e.getClientApplicationEntity().getLoginMail();
                Long apartmentId = e.getApartmentEntity().getId();
                String link = "localhost:8098/api/add_review?rating=%&review=%&apartmentId=" + apartmentId;

                mailSender.sendEmail("Отзыв о бронировании",
                        "Спасибо что выбрали нашу компанию, будем рады увидеть вас снова! Оставить отзыв : " + link + " С уважением rent_apartment_app",
                        loginMail);

                e.setSchedulerMailReview("true");
                bookingHistoryRepository.save(e);
            }
        }
    }
}
