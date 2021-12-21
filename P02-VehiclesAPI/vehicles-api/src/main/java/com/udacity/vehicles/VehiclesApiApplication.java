package com.udacity.vehicles;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
//@EnableDiscoveryClient // not needed as we are looking to use Ribbon backed Rest Query
//@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
public class VehiclesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclesApiApplication.class, args);
    }




    /**
     * Initializes the car manufacturers available to the Vehicle API.
     * @param repository where the manufacturer information persists.
     * @return the car manufacturers to add to the related repository
     */
    @Bean
    CommandLineRunner GGinitDatabase(ManufacturerRepository repository) {
        return args -> {
            repository.save(new Manufacturer(100, "Audi"));
            repository.save(new Manufacturer(101, "Chevrolet"));
            repository.save(new Manufacturer(102, "Ford"));
            repository.save(new Manufacturer(103, "BMW"));
            repository.save(new Manufacturer(104, "Dodge"));
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
     * Web Client for the maps (location) API
     * @param endpoint where to communicate for the maps API
     * @return created maps endpoint
     */
    @LoadBalanced // Tell Spring Cloud to create a Ribbon backed  ResTemplate class
    @Bean(name="maps")
    public WebClient webClientMaps(@Value("${maps.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }



    /**
     * //https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-webclient.html
     *If you have Spring WebFlux on your classpath, you can also choose to use WebClient to call remote REST services.
     *Compared to RestTemplate, this client has a more functional feel and is fully reactive.
     *You can create your own client instance with the builder, WebClient.create()
     *
     * Web Client for the pricing API
     * @param endpoint where to communicate for the pricing API
     * @return created pricing endpoint
     */
    @LoadBalanced // Tell Spring Cloud to create a Ribbon backed  ResTemplate class
    @Bean(name="pricing")
    public WebClient webClientPricing(@Value("${pricing.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

}
