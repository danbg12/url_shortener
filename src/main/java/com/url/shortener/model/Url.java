package com.url.shortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "original_url", nullable = false, unique = true)
    @Size(min = 30, max = 2048, message = "URL must be between 30 and 2048 characters")
    private String originalUrl;
    @Column(name = "short_url", unique = true, nullable = false)
    private String shortUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date_time")
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "url_hash_code", unique = true, nullable = false)
    private String urlHashCode;
}
