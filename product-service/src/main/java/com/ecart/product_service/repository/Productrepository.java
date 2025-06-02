package com.ecart.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecart.product_service.Entity.Product;

@Repository
public interface Productrepository extends JpaRepository<Product, Long> {

}
