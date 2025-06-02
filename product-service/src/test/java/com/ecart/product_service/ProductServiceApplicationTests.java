package com.ecart.product_service;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilderSupport;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecart.product_service.dto.ProductRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	 @Container
	    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
	    		 .withDatabaseName("ecartDB")
	    	        .withUsername("root")
	    	        .withPassword("siva1997");
	 
	 @Autowired
	 private MockMvc mockmvc;
	 
	 @DynamicPropertySource
	 static void setProperties(DynamicPropertyRegistry registry) {
	     registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
	     registry.add("spring.datasource.username", mysqlContainer::getUsername);
	     registry.add("spring.datasource.password", mysqlContainer::getPassword);
	 }

	 @Test
	 void shouldCreateProducts() throws Exception {
	     ProductRequest productRequest = getProductRequest();

	     mockmvc.perform(MockMvcRequestBuilders.post("/api/products")
	             .contentType(MediaType.APPLICATION_JSON)
	             .content(new ObjectMapper().writeValueAsString(productRequest)))
	             .andExpect(status().isCreated());
	 }

	private ProductRequest getProductRequest() {
		// TODO Auto-generated method stub
		return ProductRequest.builder()
				             .name("i phone 13")
				             .description("i phone 13")
				             .price(BigDecimal.valueOf(1300))
				             .build();
	}

}
