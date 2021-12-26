package com.udacity.vehicles;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Launches a Spring Boot application for the Vehicles API,
 * initializes the car manufacturers in the database,
 * and launches web clients to communicate with maps and pricing.
 *
 * Here we use @EnableJpaAuditing . Auditing of entities in an application is a crucial part to store information about the updated time and authors of changes made to the entity.
 * It helps us in tracking and logging user activity across the application.
 *
 *  spring data jpa works well together with @EnableJpaAuditing.
 * @EnableJpaAuditing maintains the audit log (automatic auditing of entities)
 *
 * THen in your Entity object , you add annotation like that above your attribute :
 *      + @Column(name = "created_date", nullable = false, updatable = false)
 *      + @Column(name = "modified_date")
 *      + @Column(name = "created_by")
 *
 * Read more here : https://medium.com/@manika09singh/enable-auditing-using-spring-data-jpa-2f62587ccb23
 *
 *****/
//@EnableEurekaClient // we register that service with Eureka for flexibility purpose. Although its not required as we use LoadBalancerClient
//@EnableDiscoveryClient// not needed as we are looking to use Ribbon backed Rest Query //https://examples.javacodegeeks.com/enterprise-java/spring/spring-cloud-feign-client-example/
@SpringBootApplication
@EnableJpaAuditing
public class VehiclesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclesApiApplication.class, args);
    }




    /**
     * Initializes the car manufacturers available to the Vehicle API.
     * @param manufRrepository where the manufacturer information persists.
     * @return the car manufacturers to add to the related repository
     */
    @Bean
    CommandLineRunner GGinitDatabase(ManufacturerRepository manufRrepository, CarRepository carRepository) {
        return args -> {
            manufRrepository.save(new Manufacturer(100, "Audi"));
            manufRrepository.save(new Manufacturer(101, "Chevrolet"));
            manufRrepository.save(new Manufacturer(102, "Ford"));
            manufRrepository.save(new Manufacturer(103, "BMW"));
            manufRrepository.save(new Manufacturer(104, "Dodge"));

            carRepository.save(getCar());
        };
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }



    /**
     * //https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-webclient.html
     * If you have Spring WebFlux on your classpath, you can also choose to use WebClient to call remote REST services.
     * Compared to RestTemplate, this client has a more functional feel and is fully reactive.
     * You can create your own client instance with the builder, WebClient.create()
     *
     * That is because, the WebClient provides three different ways to build a WebClient :
     *  1) use WebClient.create();
     *  2) use webClientBuilder();
     *
     * Web Client for the maps (location) API
     * @param endpoint where to communicate for the maps API
     * @return created maps endpoint
     */
    //https://spring.getdocs.org/en-US/spring-cloud-docs/spring-cloud-commons/cloud-native-applications/spring-cloud-commons:-common-abstractions/webclinet-loadbalancer-client.html
    //LoadBalancer is not activated with Webclient by default
    //@LoadBalanced // Tell Spring Cloud to create a Ribbon backed  ResTemplate class
    @Bean(name="maps")
    public WebClient webClientMaps(@Value("${maps.endpoint}") String endpoint, LoadBalancerClient lbClient) {

        return WebClient
                .builder()
                .baseUrl(endpoint)
                .filter(new LoadBalancerExchangeFilterFunction(lbClient))
                .build();
    }




    /**
     * //https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-webclient.html
     * If you have Spring WebFlux on your classpath, you can also choose to use WebClient to call remote REST services.
     * Compared to RestTemplate, this client has a more functional feel and is fully reactive.
     * You can create your own client instance with the builder, WebClient.create()
     *
     * That is because, the WebClient provides three different ways to build a WebClient :
     *  1) use WebClient.create();
     *  2) use webClientBuilder();
     *
     * Web Client for the pricing API
     * @param endpoint where to communicate for the pricing API
     * @return created pricing endpoint
     */
    //https://spring.getdocs.org/en-US/spring-cloud-docs/spring-cloud-commons/cloud-native-applications/spring-cloud-commons:-common-abstractions/webclinet-loadbalancer-client.html
    //LoadBalancer is not activated with Webclient by default
    //@LoadBalanced // Tell Spring Cloud to create a Ribbon backed  ResTemplate class
    @Bean(name="pricing")
    public WebClient webClientPricing(@Value("${pricing.endpoint}") String endpoint, LoadBalancerClient lbClient) {

        return WebClient
                .builder()
                .baseUrl(endpoint)
                .filter(new LoadBalancerExchangeFilterFunction(lbClient))
                .build();
    }



    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    public Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(10.730610, -23.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(103, "BMW");
        details.setManufacturer(manufacturer);
        details.setModel("Z4");
        details.setMileage(32280);
        details.setExternalColor("black");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.NEW);
        return car;
    }


}
