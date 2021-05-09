package com.currencyconverter.service;

import com.currencyconverter.dto.ForexRateDTO;

import java.math.BigDecimal;

/**
 * Performs currency conversion business logic
 */
public interface CurrencyConverterService {
    
    /**
     *
     * @param sourceCurrencyCode the source currency from which the conversion is desired
     * @param targetCurrencyCode the target currency in which the conversion is desired
     * @param amount the amount to be converted
     * @return dto with converted amount
     */
    ForexRateDTO convert(String sourceCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}
