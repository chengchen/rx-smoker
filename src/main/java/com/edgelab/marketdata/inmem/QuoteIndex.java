package com.edgelab.marketdata.inmem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteIndex {

    private String key;

    private Quote q;

}
