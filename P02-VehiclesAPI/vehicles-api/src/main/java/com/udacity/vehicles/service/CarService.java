package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;

import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 *
 */
@Service
public class CarService {


    private final CarRepository repository;
    private MapsClient mapsClient;
    private PriceClient priceClient;



    //https://www.amitph.com/introduction-to-spring-webclient/
    //https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
    public CarService(CarRepository repository, @Qualifier("pricing")WebClient pricingWebClient, @Qualifier("maps")WebClient mapsWebClient, ModelMapper modelMapper) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.priceClient= new PriceClient(pricingWebClient);
        this.mapsClient = new MapsClient(mapsWebClient, modelMapper);
        //this.pricingWebClient=pricingWebClient;

    }






    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }







    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Car car = null;
        // Load some user data asynchronously, e.g. from a DB:



        car = repository.findById(id).orElseThrow(CarNotFoundException::new);


        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         **/
        //http://localhost:8082/services/price?vehicleId=18
        // BEST : https://reflectoring.io/spring-webclient/
        //we can handle requests by weaving transforms around our Mono or Flux values, to handle and combine values as they’re returned,
        // and then pass these Flux-wrapped values into other non-blocking APIs, all fully asynchronously.
        //................................................................................
        //http://localhost:8082/services/price/4
        //http://localhost:8082/services/price/search/findPriceByVehicleId?vehicleId=1



        String price = priceClient.getPrice(id );
        car.setPrice(price);


        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */


        Double lat = car.getLocation().getLat();
        Double lon = car.getLocation().getLon();
        Location location = mapsClient.getAddress(new Location(lat, lon) );
        car.setLocation(location);

        return car;
    }













    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {

        System.out.println("....................................>We are in carService :: save() car");
        System.out.println("...Car details is :\n"+car.toString());

        Car vCar;

        if (car.getId() != null) {

            System.out.println("=>.........CardID provided, So I will try to update wha twe have");
            System.out.println("=>.........Please note!!! Only car details + Location would be Updated !");

            return repository.findById(car.getId()) // return an Optional<T>, so probably here Optional< List<Car> > so we can use map (there must be a Stream here)
                    .map(carToBeUpdated -> {    // As part of the Lambda expression .map( x -> { doDomeTHing; });  x is the object representing class T (i.e Car)
                        carToBeUpdated.setDetails(car.getDetails()); // get details from the car object being passed into param. here Details represent body + model
                        carToBeUpdated.setLocation(car.getLocation());// get Location from the car object being passed into param.  here Latitude + Longitude (rest is optional as it is managed the MapService app)

                        System.out.println("...==> car details provided for update:\n"+car.getDetails());
                        System.out.println("...==> The Location provided for update :\n"+car.getLocation());
                        System.out.println("...==> The car to be saved is :\n"+carToBeUpdated.toString());

                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }else {

            System.out.println("=>.........I CANT find this car ID, so I will create a new one");

            vCar= repository.save(car);

            if ( vCar!= null ){

               System.out.println("...==> The car saved is :\n"+vCar.toString());

               return vCar;
            }else{

                throw new CarNotFoundException(); // Throw critical error
            }

        }

    }















    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */

        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Delete the car from the repository.
         */
        System.out.println("=>....................................->trying tp delete in Service the car : "+car.toString());

        repository.deleteById(car.getId());


    }











    

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    public Car getCarView() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }



}
