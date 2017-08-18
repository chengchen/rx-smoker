package com.edgelab.marketdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "stocks")
@Setter
@Getter
@ToString
public class Stock {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH}, fetch = LAZY)
    private Issuer issuer;

}
