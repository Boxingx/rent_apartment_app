package com.example.product_module.application_exceptions;

import lombok.Getter;

@Getter
public class BookingHistoryException extends RuntimeException {

    private final String BOOKING_EXCEPTION_MESSAGE = "Бронирования текущих апартаментов не обнаружено";

}
