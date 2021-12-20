package com.udacity.pricing.domain.price;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

// How to customize Spring Data ? https://docs.spring.io/spring-data/rest/docs/current-SNAPSHOT/reference/html/#reference
//How to customize JPA Query ? https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
//INSERT INTO price (vehicleId, currency, price) VALUES (1, 'USD',13402.83);
//@Repository
@RepositoryRestResource(path = "price")
public interface PriceRepository extends CrudRepository<Price, Long> {


    @Query(value ="select d.vehicleId, d.currency, d.price from Price d where d.vehicleId = :vehicleId")
    Price findPriceByVehicleId( @Param("vehicleId") Long vehicleId);

    @Query(value ="select d.vehicleId, d.currency, d.price from Price d")
    //@RestResource(path = "test") // http://localhost:8082/services/search/test
    Stream<Price> findAllPrice();

    @Query(value ="select d.vehicleId, d.currency, d.price from Price d where d.currency=?1")
    Stream<Price> findPriceByCurrency(String currencyName);


}
