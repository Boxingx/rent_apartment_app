package com.example.rent_module.repository;

import com.example.rent_module.model.entity.PromoCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCodeEntity, Long> {

    PromoCodeEntity getPromoCodeEntityByPromoCode(String promoCode);

}
