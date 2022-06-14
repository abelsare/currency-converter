package com.currencyconverter.serviceimpl;


import com.currencyconverter.service.ExchangeRateParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

/**
 * Fetches latest exchange rates from ECB at the service start
 */
@Service
public class ConversionRatesFetcher {
    
    private ExchangeRateParserService exchangeRateParserService;
    
    @Value("${exchangeRates.url}")
    private String exchangeRateUrl;
    
    @Autowired
    public ConversionRatesFetcher(ExchangeRateParserService exchangeRateParserService) {
        this.exchangeRateParserService = exchangeRateParserService;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void downloadLatestExchangeRates() throws ParserConfigurationException, IOException, SAXException {
        var dbf = DocumentBuilderFactory.newInstance();
        var db = dbf.newDocumentBuilder();
        var document = db.parse(new URL(exchangeRateUrl).openStream());
        exchangeRateParserService.parseAndPersistExchangeRates(document);
    }
}
