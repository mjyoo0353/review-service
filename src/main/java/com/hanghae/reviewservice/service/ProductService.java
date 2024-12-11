package com.hanghae.reviewservice.service;

import com.hanghae.reviewservice.dto.ProductRequestDto;
import com.hanghae.reviewservice.dto.ProductResponseDto;
import com.hanghae.reviewservice.entity.Product;
import com.hanghae.reviewservice.repository.ProductRepository;
import com.hanghae.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    //상품에 대한 리뷰 수와 점수 업데이트
    @Transactional //리뷰수와 점수 업데이트는 하나의 트랜잭션으로 처리
    public void updateReviewCountAndScore(Long productId, Long reviewCount, double score) {
        productRepository.updateReviewCountAndScore(productId, reviewCount, score);
    }

}
