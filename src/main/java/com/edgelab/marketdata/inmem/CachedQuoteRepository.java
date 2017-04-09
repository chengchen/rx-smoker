package com.edgelab.marketdata.inmem;

import org.springframework.data.repository.CrudRepository;

public interface CachedQuoteRepository extends CrudRepository<Quote, Integer> {

}
