package com.ecart.product_service.service;

import java.util.List;

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.stereotype.Service;

import com.ecart.product_service.Entity.Product;
import com.ecart.product_service.dto.ProductRequest;
import com.ecart.product_service.dto.productResponse;
import com.ecart.product_service.repository.Productrepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final Productrepository productrepository;

	public void createProduct(ProductRequest productrequest) {
		Product product = Product.builder().name(productrequest.getName()).description(productrequest.getDescription())
				.price(productrequest.getPrice()).build();

		productrepository.save(product);
		log.info("Product is saved {}", product.getId());
	}

	public List<productResponse> getAllproduct() {
		// TODO Auto-generated method stub
		List<Product> products=productrepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
		
	}

	private productResponse mapToProductResponse(Product product) {
		// TODO Auto-generated method stub
		return productResponse.builder()
				              .id(product.getId())
				              .name(product.getName())
				              .description(product.getDescription())
				              .price(product.getPrice())
				              .build();
	}
}
