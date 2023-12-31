package com.example.product_module.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "booking_history")
@Data
public class BookingHistoryEntity {

    @Id
    @SequenceGenerator(name = "booking_historySequence", sequenceName = "booking_history_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_historySequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    private ApartmentEntity apartmentEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private ClientApplicationEntity clientApplicationEntity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product")
    private ProductEntity productEntity;

    @Column(name = "days_count")
    private Long daysCount;

    @Column(name = "final_payment")
    private Double finalPayment;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "scheduler_processing")
    private String schedulerProcessing;

    @Column(name = "scheduler_mail_review")
    private String schedulerMailReview;
}
