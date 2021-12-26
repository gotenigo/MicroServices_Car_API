package com.udacity.vehicles;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.domain.car.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


//@MockBean works well with the Mockito library.
//@WebMvcTest is used for controller layer unit testing.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // define the
@RunWith(SpringRunner.class)
//@SpringBootTest
public class VehiclesApiApplicationTests {
/*
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        return objectMapper;
    }*/


    @Test
    public void contextLoads() {
    }


    @LocalServerPort
    private int port;

    @Autowired // auto provided by Springboot and allow us to consume Rest Api
    private TestRestTemplate restTemplate;






    @Test
    public void getAllCars() throws Exception {

        //https://stackoverflow.com/questions/9381665/how-can-we-configure-the-internal-jackson-mapper-when-using-resttemplate
        //https://stackoverflow.com/questions/31753708/spring-resttemplate-and-json-how-to-ignore-empty-arrays-deserialization


        ObjectMapper ojectMapper = new ObjectMapper();
        ojectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        ojectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        ojectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        RestTemplate template = new RestTemplateBuilder()
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(ojectMapper ))
                .build();

        @SuppressWarnings("unchecked")
        ResponseEntity<List<Object>> response =    template.exchange("http://localhost:" + port + "/cars", HttpMethod.GET, null, new   ParameterizedTypeReference<List<Object>>() {});
        //ResponseEntity<List<Car>> response = restTemplate.exchange("http://localhost:" + port + "/cars", myBean);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }





}
