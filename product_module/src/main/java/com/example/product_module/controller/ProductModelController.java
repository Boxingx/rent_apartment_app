package com.example.product_module.controller;


import com.example.product_module.email_sender.MailSender;

import com.example.product_module.service.ProductModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.product_module.constant_project.ConstantProject.ADD_PRODUCT;

@RestController
@RequiredArgsConstructor
public class ProductModelController {

    private final MailSender mailSender;

    private final ProductModuleService productModuleService;


    @GetMapping("/product/test")
    public String testModule(@RequestParam String subject, @RequestParam String text) {
        mailSender.sendEmail(subject, text, null);
        String s = "Эта строка получена из другого модуля";
        return s;
    }

    @GetMapping(ADD_PRODUCT)
    public Double addProductForBooking(@RequestParam Long id) {
        return productModuleService.prepareProduct(id);
    }
}
