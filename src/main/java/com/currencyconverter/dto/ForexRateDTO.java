package com.currencyconverter.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ForexRateDTO {
    
    private String referenceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal convertedAmount;
    
}
