package com.example.rent_module.config;

public class ValidationErrorDetails {
        private String field;
        private String message;

        public ValidationErrorDetails(String field, String message) {
            this.field = field;
            this.message = message;
        }
}
