package com.edgelab.marketdata.inmem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("indexes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteIndex {

    @Id
    private String key;

    @Reference
    private Quote q;

}
