package com.freelancehub.review_service.entity;

import com.freelancehub.review_service.enums.ReviewType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contractId;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private String reviewerEmail;

    @Column(nullable = false)
    private Long revieweeId;

    @Column(nullable = false)
    private String revieweeEmail;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewType type;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
