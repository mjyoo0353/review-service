package com.hanghae.reviewservice.entity;

import com.hanghae.reviewservice.dto.ReviewRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "product_id"})})
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String content;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private double score; //리뷰 점수 (1-5)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Review(ReviewRequestDto requestDto, Product product) {
        this.product = product;
        this.userId = requestDto.getUserId();
        this.content = requestDto.getContent();
        this.score = requestDto.getScore();
        validateScore(this.score); // 리뷰 점수 확인
    }

    // 리뷰 점수가 1-5점 사이가 아니면 예외처리 내용 반환
    private void validateScore(double score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("1-5점 사이의 점수를 입력해주세요.");
        }
    }

}
