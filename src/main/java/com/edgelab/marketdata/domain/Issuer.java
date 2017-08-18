package com.edgelab.marketdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stock_issuers")
@Setter
@Getter
@ToString
public class Issuer {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

}
