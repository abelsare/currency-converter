package com.currencyconverter.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ForexRateDTO {
    
    private String referenceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
}
