package com.currencyconverter.serviceimpl;

import com.currencyconverter.dao.ForexRateRepository;
import com.currencyconverter.dto.ForexRateDTO;
import com.currencyconverter.entity.ForexRate;
import com.currencyconverter.exception.ExchangeRateNotFoundException;
import com.currencyconverter.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {
    
    private ForexRateRepository forexRateRepository;
    
    @Autowired
    public CurrencyConverterServiceImpl(ForexRateRepository forexRateRepository) {
        this.forexRateRepository = forexRateRepository;
    }
    
    @Override
    public ForexRateDTO convert(String sourceCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        if(sourceCurrencyCode.isBlank() || targetCurrencyCode.isBlank() || Objects.isNull(amount)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "All query parameters required");
        }
        
        var convertedAmount = calculateAmount(sourceCurrencyCode, targetCurrencyCode, amount);
        
        return ForexRateDTO.builder()
                .convertedAmount(convertedAmount)
                .referenceCurrencyCode(sourceCurrencyCode)
                .targetCurrencyCode(targetCurrencyCode)
                .build();
    }
    
    private BigDecimal calculateAmount(String sourceCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        BigDecimal convertedAmount = null;
        
        if(sourceCurrencyCode.equalsIgnoreCase(targetCurrencyCode)) {
            // No need to convert the amount in this case
            convertedAmount = amount;
        } else {
            if(sourceCurrencyCode.equals("EUR")) {
                // Find the target currency rate and multiply with the amount
                var targetRate = forexRateRepository.findByTargetCurrencyCode(targetCurrencyCode);
                throwExceptionIfNull(targetRate, targetCurrencyCode);
                convertedAmount = amount.multiply(targetRate.getExchangeRate());
            } else if(targetCurrencyCode.equals("EUR")) {
                // Find the source currency rate and divide the amount with it
                var sourceRate = forexRateRepository.findByTargetCurrencyCode(sourceCurrencyCode);
                throwExceptionIfNull(sourceRate, sourceCurrencyCode);
                convertedAmount = amount.divide(sourceRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
            } else {
                // Perform triangulation as none of the currencies are reference currency
                convertedAmount = triangulate(sourceCurrencyCode, targetCurrencyCode, amount);
            }
        }
        return convertedAmount;
    }
    
    private BigDecimal triangulate(String sourceCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        var sourceRate = forexRateRepository.findByTargetCurrencyCode(sourceCurrencyCode);
        var targetRate = forexRateRepository.findByTargetCurrencyCode(targetCurrencyCode);
        throwExceptionIfNull(sourceRate, sourceCurrencyCode);
        throwExceptionIfNull(targetRate, targetCurrencyCode);
        var convertedAmount = amount.divide(sourceRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
        return convertedAmount.multiply(targetRate.getExchangeRate());
    }
    
    private void throwExceptionIfNull(ForexRate forexRate, String currencyCode) {
        if(Objects.isNull(forexRate)) {
            throw new ExchangeRateNotFoundException(
                    "Could not convert as exchange rate for code : " + currencyCode + " not found.");
        }
    }
    
    
}
