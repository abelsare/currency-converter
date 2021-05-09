package com.currencyconverter.serviceimpl;

import com.currencyconverter.dto.ForexRateDTO;
import com.currencyconverter.service.CurrencyConverterService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {
    
    @Override
    public ForexRateDTO convert(String sourceCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        return null;
    }
}
