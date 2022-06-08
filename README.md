# currency-converter
Provides conversion from source to target currencies. Uses daily rates from European Central Bank (ECB) for conversion. The rates get stored in H2 database when the service boots up. Performs triangulation in case source currency is different than EUR. For eg: when converting from USD to INR, the service performs USD -> EUR -> INR as the base currency in ECB data is always EUR.

# Technologies
1. Java 11
2. Spring Boot
3. H2 Database
4. Docker
5. Maven

#Usage
GET <baseurl>/forexRates/conversion?from=<sourceCurrencyCode>&to=<targetCurrencyCode>&amount=<amount>

Eg: GET http://localhost:8080/forexRates/conversion?from=USD&to=INR&amount=1000


