package com.udacity.pricing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {



	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return objectMapper;
	}


	@LocalServerPort
	private int port;



	@Test
	public void contextLoads() {
	}


	@Test
	public void getAllPrice() throws Exception {

		RestTemplate restTemplate = new RestTemplateBuilder()
				.additionalMessageConverters(new MappingJackson2HttpMessageConverter( objectMapper() ))
				.build();

		@SuppressWarnings("unchecked")
		ResponseEntity<List<Object>> response =    restTemplate.exchange("http://localhost:" + port + "/services/price", HttpMethod.GET, null, new   ParameterizedTypeReference<List<Object>>() {});

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}





}
