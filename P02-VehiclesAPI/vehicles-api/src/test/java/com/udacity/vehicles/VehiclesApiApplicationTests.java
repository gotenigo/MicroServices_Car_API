package com.udacity.vehicles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


//@MockBean works well with the Mockito library.
//@WebMvcTest is used for controller layer unit testing.
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehiclesApiApplicationTests {

    @Test
    public void contextLoads() {
    }

}
