package com.hanghae.reviewservice.entity;

import com.hanghae.reviewservice.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewCount;

    @Column(nullable = false)
    private double score; //평균 점수

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;


    public Product(ProductRequestDto requestDto) {
        this.reviewCount = requestDto.getReviewCount();
        this.score = requestDto.getScore();
    }


}
