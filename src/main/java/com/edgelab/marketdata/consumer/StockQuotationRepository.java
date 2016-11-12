package com.edgelab.marketdata.consumer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface StockQuotationRepository extends PagingAndSortingRepository<StockQuotation, UUID> {

    List<StockQuotation> findByStock(@Param(value = "stock") String stock);

}
