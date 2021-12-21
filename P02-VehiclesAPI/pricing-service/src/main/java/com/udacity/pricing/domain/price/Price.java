package com.udacity.pricing.domain.price;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {

    @Id
    @Column(name = "VEHICLEID") // In the database, we use the name VEHICLEID and not Id as per the default Spring setup. As such, we need that setting
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vehicleId;

    private String currency;
    private BigDecimal price;


    public Price() {
    }

    public Price(String currency, BigDecimal price, Long vehicleId) {
        this.currency = currency;
        this.price = price;
        this.vehicleId = vehicleId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }


    @Override
    public String toString() {
        return "Price{" +
                "currency='" + currency + '\'' +
                ", price=" + price +
                ", vehicleId=" + vehicleId +
                '}';
    }
}
