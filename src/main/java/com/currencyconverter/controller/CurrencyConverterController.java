package com.currencyconverter.controller;

import com.currencyconverter.dto.ForexRateDTO;
import com.currencyconverter.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Exposes application api endpoints
 */
@RestController
@RequestMapping("/forexRates")
public class CurrencyConverterController {
    
    private CurrencyConverterService currencyConverterService;
    
    @Autowired
    public CurrencyConverterController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }
    
    @GetMapping("/conversion")
    public ForexRateDTO convert(@RequestParam("from") String sourceCurrencyCode,
                                @RequestParam("to") String targetCurrencyCode,
                                @RequestParam("amount") BigDecimal amount) {
        return currencyConverterService.convert(sourceCurrencyCode, targetCurrencyCode, amount);
    }
}
