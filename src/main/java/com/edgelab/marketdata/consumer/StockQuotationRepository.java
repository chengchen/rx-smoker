package com.edgelab.marketdata.consumer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockQuotationRepository extends RevisionRepository<StockQuotation, UUID, Integer>, PagingAndSortingRepository<StockQuotation, UUID> {
}
