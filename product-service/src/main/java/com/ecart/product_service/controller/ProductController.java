package com.ecart.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecart.product_service.dto.ProductRequest;
import com.ecart.product_service.dto.productResponse;
import com.ecart.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productservice;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(@RequestBody ProductRequest productrequest) {

		productservice.createProduct(productrequest);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<productResponse>getAllProduct(){
		return productservice.getAllproduct();
	}
}
