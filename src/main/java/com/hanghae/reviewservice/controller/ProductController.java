package com.hanghae.reviewservice.controller;

import com.hanghae.reviewservice.dto.*;
import com.hanghae.reviewservice.service.ProductService;
import com.hanghae.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    //상품 등록
    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    //상품에 대한 리뷰 등록 API
    @PostMapping("/{productId}/reviews")
    public ReviewResponseDto createReview(@PathVariable Long productId,
                                          @Valid @RequestBody ReviewRequestDto requestDto) {
        return reviewService.createReview(productId, requestDto);
    }

}
