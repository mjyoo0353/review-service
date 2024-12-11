package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
