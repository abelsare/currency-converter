package com.currencyconverter.serviceimpl;

import com.currencyconverter.dao.ForexRateRepository;
import com.currencyconverter.entity.ForexRate;
import com.currencyconverter.service.ExchangeRateParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExchangeRateParserServiceImpl implements ExchangeRateParserService {
    
    private ForexRateRepository forexRateRepository;
    
    private static final String REFERENCE_CURRENCY_CODE = "EUR";
    private static final String RATE_ATTRIBUTE = "rate";
    private static final String CURRENCY_ATTRIBUTE = "currency";
    private static final String CUBE_NODE = "Cube";
    
    
    @Autowired
    public ExchangeRateParserServiceImpl(ForexRateRepository forexRateRepository) {
        this.forexRateRepository = forexRateRepository;
    }
    
    @Override
    public void parseAndPersistExchangeRates(Document exchangeRateDoc) {
        var cubeNodes = exchangeRateDoc.getElementsByTagName(CUBE_NODE);
        var rateNodes = getRateNodes(cubeNodes);
        rateNodes.forEach(this::persistExchangeRate);
    }
    
    private void persistExchangeRate(Element rateNode) {
        var exchangeRate = ForexRate.builder()
                .referenceCurrencyCode(REFERENCE_CURRENCY_CODE)
                .targetCurrencyCode(rateNode.getAttribute(CURRENCY_ATTRIBUTE))
                .exchangeRate(new BigDecimal(rateNode.getAttribute(RATE_ATTRIBUTE)))
                .build();
        if(Objects.nonNull(exchangeRate)) {
            forexRateRepository.save(exchangeRate);
        }
    }
    
    private List<Element> getRateNodes(NodeList cubeNodes) {
        List<Element> rateNodes = new ArrayList<>();
        for(int i=0; i< cubeNodes.getLength(); i++) {
            Element cubeNode = (Element) cubeNodes.item(i);
            if(cubeNode.hasAttribute(RATE_ATTRIBUTE)) {
                rateNodes.add(cubeNode);
            }
        }
        return rateNodes;
    }
}
