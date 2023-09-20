package com.example.rent_module.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "promo_code_info")
@Data
@Getter
@Setter
public class PromoCodeEntity {

    @Id
    @SequenceGenerator(name = "promo_code_infoSequence", sequenceName = "promo_code_info_sequence", allocationSize = 1, initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promo_code_infoSequence")
    @Column(name = "promo_id")
    private Long id;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "status")
    private String status;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "discount_type")
    private String discountType;

}
