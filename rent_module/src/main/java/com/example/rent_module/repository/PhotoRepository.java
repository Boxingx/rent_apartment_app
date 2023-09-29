package com.example.rent_module.repository;

import com.example.rent_module.model.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<PhotoEntity,Long> {

    PhotoEntity getPhotoEntityById(Long id);

    List<PhotoEntity> findPhotoEntitiesByApartmentEntity_Id(Long id);

    @Query(value = "select p from PhotoEntity p where p.apartmentEntity.id = :id")
    List<PhotoEntity> findPhotoEntitiesByApartmentEntity(Long id);

    @Query(value = "select COUNT(p) from PhotoEntity p where p.apartmentEntity.id = :id")
    Integer findPhotoEntitiesByApartmentEntityCount(Long id);
}
