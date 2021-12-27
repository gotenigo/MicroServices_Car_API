package com.udacity.vehicles.api;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@ApiResponse is used  by swagger to customize the default response
@ApiResponses(value = {
        @ApiResponse(code=400, message = "This is a bad request, please follow the API documentation for the proper request format."),
        @ApiResponse(code=401, message = "Due to security constraints, your access request cannot be authorized. "),
        @ApiResponse(code=500, message = "The server is down. Please make sure that the Location microservice is running.")
})

//consumes = "application/json"
/**
 * Implements a REST-based controller for the Vehicles API.
 */

@RestController
@RequestMapping(path="/cars" , produces = { "application/json" } ) // Force the output format to JSON. ALso help to workaround issue with the some browser that does not specify the Media TYpe on GET
class CarController {


    // using the  Cart service + CarResourceAssembler to publish data
    private final CarService carService;
    private final CarResourceAssembler assembler;

    @Autowired
    private DiscoveryClient discoveryClient;

    //Constructor
    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }






    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @GetMapping
    Resources<Resource<Car>> list() {

        List<Car> carList =carService.list();

        List<Resource<Car>> resources = carList.stream().map(assembler::toResource)
                .collect(Collectors.toList());

        System.out.println("...GG says, the list is ="+ carList );

        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }







    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    //You can also change the Media Type supported on your GET
    //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc
    @GetMapping("/{id}")
    Resource<Car> get(@PathVariable Long id) {
        /**
         * TODO: Use the `findById` method from the Car Service to get car information.
         * TODO: Use the `assembler` on that car and return the resulting output.
         *   Update the first line as part of the above implementing.
         */
        System.out.println("=> we are inside GEt  Car with Id : "+id);
        return assembler.toResource(carService.findById(id)/*carService.getCarView()*/);
    }






    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {
        /**
         * TODO: Use the `save` method from the Car Service to save the input car.
         * TODO: Use the `assembler` on that saved car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */

        System.out.println("=>We are in the post() Car");

        Car vcar = carService.save(car); // we save car into thr database via CarRepository that uses JPA (Crud)

        Resource<Car> resource = assembler.toResource(vcar/*new Car()*/);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }













    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        /**
         * TODO: Set the id of the input car object to the `id` input.
         * TODO: Save the car using the `save` method from the Car service
         * TODO: Use the `assembler` on that updated car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */

        System.out.println("=.>We are in the put() Car");

        car.setId(id);
        carService.save(car); // we save car into thr database via CarRepository that uses JPA (Crud)

        Resource<Car> resource = assembler.toResource(car);
        return ResponseEntity.ok(resource); // we return the status OK with the created status Entity
    }




    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        /**
         * TODO: Use the Car Service to delete the requested vehicle.
         */

        System.out.println("=>We are in the delete() Car");

        carService.delete(id);

        return ResponseEntity.noContent().build();
    }




    //Test out the DiscoveryClient functionality
    @GetMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }



    //Test out the DiscoveryClient functionality
    @GetMapping("/service-instances")
    public List<String> serviceInstances() {
        return this.discoveryClient.getServices();
    }






}
