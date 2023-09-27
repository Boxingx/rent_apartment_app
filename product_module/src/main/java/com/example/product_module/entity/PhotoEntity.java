package com.example.product_module.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "photo_info")
@Data
public class PhotoEntity {

    @Id
    @SequenceGenerator(name = "photo_infoSequence", sequenceName = "photo_info_sequence", allocationSize = 1, initialValue = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_infoSequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "image_data")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private ApartmentEntity apartmentEntity;
}
