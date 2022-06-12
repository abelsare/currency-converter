package com.currencyconverter.controller;

import com.currencyconverter.dto.ForexRateDTO;
import com.currencyconverter.exception.ExchangeRateNotFoundException;
import com.currencyconverter.exception.GlobalExceptionHandler;
import com.currencyconverter.service.CurrencyConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CurrencyConverterControllerTest {
    
    @Mock
    private CurrencyConverterService currencyConverterService;
    
    private MockMvc mockMvc;
    
    @InjectMocks
    private CurrencyConverterController currencyConverterController;
    
    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyConverterController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }
    
    @Test
    public void convert() throws Exception {
        ForexRateDTO forexRateDTO = ForexRateDTO.builder()
                .convertedAmount(BigDecimal.ONE).referenceCurrencyCode("USD")
                .targetCurrencyCode("INR").build();
        when(currencyConverterService.convert("USD", "INR", BigDecimal.ONE))
                .thenReturn(forexRateDTO);
        
        mockMvc.perform(get("/forexRates/conversion?from=USD&to=INR&amount=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
    
    @Test
    public void convertException() throws Exception {
        doThrow(ExchangeRateNotFoundException.class).when(currencyConverterService).convert("USD", "INR", BigDecimal.ONE);
        
        mockMvc.perform(get("/forexRates/conversion?from=USD&to=INR&amount=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}
