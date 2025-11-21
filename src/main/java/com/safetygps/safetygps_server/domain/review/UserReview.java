package com.safetygps.safetygps_server.domain.review;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_review")
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String writerName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String fullAddress;

    @Column(nullable = false)
    private String sigunNm;

    private String gu;

    @Column(nullable = false)
    private String dong;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String writerName,
                       String content,
                       int rating,
                       String fullAddress,
                       String sigunNm,
                       String gu,
                       String dong) {
        this.writerName = writerName;
        this.content = content;
        this.rating = rating;
        this.fullAddress = fullAddress;
        this.sigunNm = sigunNm;
        this.gu = gu;
        this.dong = dong;
    }
}
