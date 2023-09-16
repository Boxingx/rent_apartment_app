package com.example.rent_module.service;


import com.example.rent_module.model.dto.AuthDto;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    String registration(@RequestBody ClientApplicationEntity clientApplicationEntity);

    String auth(@RequestBody AuthDto authDto);
}
