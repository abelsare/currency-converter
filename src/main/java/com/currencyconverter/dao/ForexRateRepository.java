package com.currencyconverter.dao;

import com.currencyconverter.entity.ForexRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides methods for data manipulation
 */
@Repository
public interface ForexRateRepository extends CrudRepository<ForexRate, Long> {
    ForexRate findByTargetCurrencyCode(String targetCurrencyCode);
}
