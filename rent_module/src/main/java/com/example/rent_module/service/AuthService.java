package com.example.rent_module.service;


import com.example.rent_module.config.AuthToken;
import com.example.rent_module.model.dto.AuthDto;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    AuthToken registration(@RequestBody ClientApplicationEntity clientApplicationEntity);

    AuthToken auth(@RequestBody AuthDto authDto);

    ClientApplicationEntity checkValidToken(String token);
}
