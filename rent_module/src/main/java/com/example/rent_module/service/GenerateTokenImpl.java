package com.example.rent_module.service;

import java.time.LocalDateTime;
import java.util.UUID;


public class GenerateTokenImpl {

    public static String generateToken() {
        return UUID.randomUUID() + "|" + LocalDateTime.now().plusDays(1);
    }
}
