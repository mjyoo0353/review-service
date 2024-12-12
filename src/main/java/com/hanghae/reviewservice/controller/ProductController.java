package com.hanghae.reviewservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.reviewservice.dto.*;
import com.hanghae.reviewservice.service.ProductService;
import com.hanghae.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    @PostMapping(value = "/{productId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ReviewResponseDto createReview(@PathVariable Long productId,
                                          @RequestPart("requestDto") String requestDtoJson,
                                          @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        //JSON 수동 파싱처리
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewRequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(requestDtoJson, ReviewRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("잘못된 데이터 형식입니다.");
        }
        return reviewService.createReview(productId, requestDto, imageFile);
    }

    /*@PostMapping("/{productId}/reviews")
    public ReviewResponseDto createReview(@PathVariable Long productId,
                                          @RequestPart("requestDto") ReviewRequestDto requestDto) {

        return reviewService.createReview(productId, requestDto);
    }*/

    //상품에 대한 리뷰 조회 API
    @GetMapping("/{productId}/reviews")
    public ReviewListResponseDto getReviewList(@PathVariable Long productId,
                                               @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return reviewService.getReviewList(productId, cursor, size);
    }
}
