package com.udacity.vehicles.domain.manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//http://localhost:8083/manufacturers
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

}
