package com.edgelab.marketdata.publisher;

import com.edgelab.marketdata.domain.Issuer;
import com.edgelab.marketdata.domain.IssuerRepository;
import com.edgelab.marketdata.domain.Stock;
import com.edgelab.marketdata.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssuerService {

    private final StockRepository stockRepository;
    private final IssuerRepository issuerRepository;

    @Transactional
    public void createIssuer(Stock stock) {
        Issuer issuer = new Issuer();
        issuer.setName("Comp");
        issuerRepository.save(issuer);

        stock.setIssuer(issuer);
        stockRepository.save(stock);
    }

}
