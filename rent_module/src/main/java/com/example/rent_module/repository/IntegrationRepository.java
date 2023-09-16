package com.example.rent_module.repository;


import com.example.rent_module.model.entity.IntegrationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRepository extends JpaRepository<IntegrationInfoEntity, Long> {
}
