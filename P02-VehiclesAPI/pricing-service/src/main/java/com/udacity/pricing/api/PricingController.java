package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implements a REST-based controller for the pricing service. So that the Pricing Service API is converted to a microservice with Spring Data REST,
 * without the need to explicitly include code for the Controller or Service.
 *
 * So how does Spring Data Rest work?
 *
 * At application startup, Spring Data Rest finds all of the spring data repositories
 * Then, Spring Data Rest creates an endpoint that matches the entity name
 * Next, Spring Data Rest appends an S to the entity name in the endpoint
 * Lastly, Spring Data Rest exposes CRUD (Create, Read, Update, and Delete) operations as RESTful APIs over HTTP
 *
 *  !! No need to have a controller or a Service with Spring Data Rest
 **/
//http://localhost:8082/services/price?vehicleId=18
//@RestController  // We are using SPING DATA, so this no longer needed. So we can remove the annotation to register with Spring
//@RequestMapping("/services/price")
public class PricingController {

    /**
     * Gets the price for a requested vehicle.
     * @param vehicleId ID number of the vehicle for which the price is requested
     * @return price of the vehicle, or error that it was not found.
     */
    @GetMapping
    public Price get(@RequestParam Long vehicleId) {
        try {
            return PricingService.getPrice(vehicleId);
        } catch (PriceException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Price Not Found", ex);
        }
    }




}
