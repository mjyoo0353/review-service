package com.hanghae.reviewservice.service;

import com.hanghae.reviewservice.dto.ProductRequestDto;
import com.hanghae.reviewservice.dto.ProductResponseDto;
import com.hanghae.reviewservice.entity.Product;
import com.hanghae.reviewservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = new Product(requestDto);
        product = productRepository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return productResponseDto;
    }

}
