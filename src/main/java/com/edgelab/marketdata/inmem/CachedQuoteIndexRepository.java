package com.edgelab.marketdata.inmem;

import org.springframework.data.repository.CrudRepository;

public interface CachedQuoteIndexRepository extends CrudRepository<QuoteIndex, String> {

    QuoteIndex findByKey(String key);

}
