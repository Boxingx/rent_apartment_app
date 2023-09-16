package com.example.rent_module.controller;


import com.example.rent_module.model.dto.AuthDto;
import com.example.rent_module.model.entity.ClientApplicationEntity;
import com.example.rent_module.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.rent_module.constant_project.ConstantProject.AUTH;
import static com.example.rent_module.constant_project.ConstantProject.REGISTRATION;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(REGISTRATION)
    public String registrationUser(@RequestBody ClientApplicationEntity clientApplicationEntity) {
        return authService.registration(clientApplicationEntity);
    }

    @PostMapping(AUTH)
    public String authUser(@RequestBody AuthDto authDto){
        return authService.auth(authDto);
    }

}
