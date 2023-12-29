package com.example.rent_module.repository;


import com.example.rent_module.model.entity.ApartmentEntity;
import com.example.rent_module.model.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> getRatingEntitiesByApartmentEntity(ApartmentEntity apartmentEntity);

    @Query(value = "select r from RatingEntity r")
    List<RatingEntity> findAllRatings();
}
