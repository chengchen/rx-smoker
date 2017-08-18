package com.edgelab.marketdata.domain;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@JaversSpringDataAuditable
public interface StockRepository extends PagingAndSortingRepository<Stock, Integer> {
}
