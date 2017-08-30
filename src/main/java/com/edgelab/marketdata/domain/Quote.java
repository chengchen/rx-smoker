package com.edgelab.marketdata.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "quotes")
public class Quote {

    @Column(name = "ticker", tag = true)
    private String ticker;

    @Column(name = "time")
    private Instant date;

    @Column(name = "value")
    private Double value;

}
