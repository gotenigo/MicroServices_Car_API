package com.udacity.pricing.domain.price;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//INSERT INTO price (vehicleId, currency, price) VALUES (1, 'USD',13402.83);
//@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {

/*
    @Query("select d.vehicleId, d.currency, d.price from Price d where d.vehicleId=:id")
    List<Price> findPriceByVehicleId(Long id);

    @Query("select d.vehicleId, d.currency, d.price from Price d")
    List<Price> findAllPrice();

    @Query("select d.vehicleId, d.currency, d.price from Price d where d.currency=:currencyName")
    List<Price> findPriceByCurrency(String currencyName);
*/

}
