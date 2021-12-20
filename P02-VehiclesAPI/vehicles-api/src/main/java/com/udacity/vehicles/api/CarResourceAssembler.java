package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.car.Car;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Maps the CarController to the Car class using HATEOAS
 *
 * => What is Spring HATEOAS ?
 * This project provides some APIs to ease creating REST representations that follow the HATEOAS principle
 * when working with Spring and especially Spring MVC.
 * The core problem it tries to address is link creation and representation assembly.
 *
 *
 * Spring HATEOAS provides ResourceAssembler interface to make it possible to use dedicated classes responsible for converting
 * domain types to REST resource types.
 * This helps us not to repeat resources and their links creation at multiple places in controller classes.
 *
 * HATEOAS (Hypermedia as the Engine of Application State) will add info like  the below in
 * the REST response to make the API more user friendly.
 *
 * The term “hypermedia” refers to any content that contains links to other forms of media such as images, movies, and text
 *
 *         {
 *             "href": "10/employees",
 *             "rel": "employees",
 *             "type" : "GET"
 *         } *
 *
 * https://howtodoinjava.com/spring-boot2/rest/rest-with-spring-hateoas-example/
 *
 */


@Component
public class CarResourceAssembler implements ResourceAssembler<Car, Resource<Car>> {

    @Override
    public Resource<Car> toResource(Car car) {
        return new Resource<>(car,
                linkTo(methodOn(CarController.class).get(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).list()).withRel("cars"));
    }

}
