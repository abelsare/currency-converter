package com.currencyconverter.service;

import org.w3c.dom.Document;

/**
 * Prases exchange rates fetched from ECB and stores in a persistence storage
 */
public interface ExchangeRateParserService {
    
    /**
     * Creates exchange rate entities and persists them to the database
     * @param exchangeRateDoc
     */
    void parseAndPersistExchangeRates(Document exchangeRateDoc);

}
