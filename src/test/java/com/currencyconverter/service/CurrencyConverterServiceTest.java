package com.currencyconverter.service;

import com.currencyconverter.dao.ForexRateRepository;
import com.currencyconverter.dto.ForexRateDTO;
import com.currencyconverter.entity.ForexRate;
import com.currencyconverter.exception.ExchangeRateNotFoundException;
import com.currencyconverter.serviceimpl.CurrencyConverterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CurrencyConverterServiceTest {
    
    @Mock
    private ForexRateRepository forexRateRepository;
    
    @Mock
    private ForexRate sourceRate;
    
    @Mock
    private ForexRate targetRate;
    
    @InjectMocks
    private CurrencyConverterServiceImpl currencyConverterService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void convert() {
        when(forexRateRepository.findByTargetCurrencyCode("USD")).thenReturn(sourceRate);
        when(forexRateRepository.findByTargetCurrencyCode("INR")).thenReturn(targetRate);
        when(sourceRate.getExchangeRate()).thenReturn(BigDecimal.valueOf(1.25));
        when(targetRate.getExchangeRate()).thenReturn(BigDecimal.valueOf(85));
        ForexRateDTO forexRateDTO = currencyConverterService.convert("USD", "INR", BigDecimal.TEN);
        assertNotNull(forexRateDTO);
        assertEquals(new BigDecimal("680.00"), forexRateDTO.getConvertedAmount());
        assertEquals("USD", forexRateDTO.getReferenceCurrencyCode());
        assertEquals("INR", forexRateDTO.getTargetCurrencyCode());
    }
    
    @Test
    public void convertWithNullData() {
        try {
            ForexRateDTO forexRateDTO = currencyConverterService.convert("USD", "INR", null);
        } catch (HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertEquals("All query parameters required", e.getStatusText());
        }
    }
    
    @Test
    public void convertWithSameCurrency() {
        ForexRateDTO forexRateDTO = currencyConverterService.convert("EUR", "EUR", BigDecimal.TEN);
        assertNotNull(forexRateDTO);
        assertEquals(BigDecimal.TEN, forexRateDTO.getConvertedAmount());
        assertEquals("EUR", forexRateDTO.getReferenceCurrencyCode());
        assertEquals("EUR", forexRateDTO.getTargetCurrencyCode());
    }
    
    @Test
    public void convertWithSourceReference() {
        when(forexRateRepository.findByTargetCurrencyCode("NZD")).thenReturn(targetRate);
        when(targetRate.getExchangeRate()).thenReturn(BigDecimal.valueOf(1.65));
        ForexRateDTO forexRateDTO = currencyConverterService.convert("EUR", "NZD", BigDecimal.TEN);
        assertNotNull(forexRateDTO);
        assertEquals(new BigDecimal("16.50"), forexRateDTO.getConvertedAmount());
        assertEquals("EUR", forexRateDTO.getReferenceCurrencyCode());
        assertEquals("NZD", forexRateDTO.getTargetCurrencyCode());
    }
    
    @Test
    public void convertWithTargetReference() {
        when(forexRateRepository.findByTargetCurrencyCode("GBP")).thenReturn(sourceRate);
        when(sourceRate.getExchangeRate()).thenReturn(new BigDecimal("0.8"));
        ForexRateDTO forexRateDTO = currencyConverterService.convert("GBP", "EUR", BigDecimal.TEN);
        assertNotNull(forexRateDTO);
        assertEquals(new BigDecimal("12.50"), forexRateDTO.getConvertedAmount());
        assertEquals("GBP", forexRateDTO.getReferenceCurrencyCode());
        assertEquals("EUR", forexRateDTO.getTargetCurrencyCode());
    }
    
    @Test
    public void convertWithNoExchangeRate() {
        when(forexRateRepository.findByTargetCurrencyCode("KWD")).thenReturn(null);
        try {
            currencyConverterService.convert("KWD", "SGD", BigDecimal.TEN);
        } catch (ExchangeRateNotFoundException e) {
            assertEquals("Could not convert as exchange rate for code : KWD not found.", e.getMessage());
        }
    }
    
}
