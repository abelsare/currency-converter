package com.currencyconverter.entity;

import com.currencyconverter.constant.QuotationType;
import lombok.Data;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a conversion rate entity in the system
 */
@Data
@Entity
@Table(indexes = @Index(columnList = "targetCurrencyCode"))
public class ForexRate {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String referenceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal exchangeRate;
    private QuotationType quotationType = QuotationType.DIRECT;
}
