package com.edgelab.marketdata.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@Table(name = "stocks")
@Setter
@Getter
@ToString
public class Stock {

    @Id
    @GeneratedValue
    private Integer id;

    private String ticker;

}
