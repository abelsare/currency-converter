package com.currencyconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ExchangeRateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleExchangeRateNotFoundException(ExchangeRateNotFoundException exchangeRateNotFoundException) {
        return exchangeRateNotFoundException.getMessage();
    }
}
