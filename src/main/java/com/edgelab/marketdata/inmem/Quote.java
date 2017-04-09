package com.edgelab.marketdata.inmem;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("quotes")
@Data
public class Quote {

    @Id
    private final String id;

    private final String value;

}
