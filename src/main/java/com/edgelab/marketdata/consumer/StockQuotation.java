package com.edgelab.marketdata.consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.UUID;

import static java.lang.String.format;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class StockQuotation {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column
    private String stock;

    @Column
    private Double value;

    @Column
    private Calendar timestamp;

    @Column
    private Double openValue;

    @Column
    private Double highValue;

    @Column
    private Double lowValue;

    @Column
    private Double volume;

    @Override
    public String toString() {
        return format("[%s]@%s (%s) H(%s)/L(%s)/O(%s)",
            this.stock,
            this.value,
            this.timestamp.getTime().toString(),
            this.highValue,
            this.lowValue,
            this.openValue);
    }

}
