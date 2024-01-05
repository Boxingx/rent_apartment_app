package com.example.rent_module.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "client_application")
@Data
public class ClientApplicationEntity {

    @Id
    @SequenceGenerator(name = "client_applicationSequence", sequenceName = "client_application_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_applicationSequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "login_mail")
    private String loginMail;

    @Column(name = "password")
    private String password;

    @Column(name = "commercial_status")
    private boolean commercialStatus;

    @Column(name = "booking_count")
    private Integer bookingCount;

    @Column(name = "parent_city")
    private String parentCity;

    @Column(name = "user_token")
    private String userToken;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clientApplicationEntity")
    private List<ApartmentEntity> apartmentEntityList;

}
