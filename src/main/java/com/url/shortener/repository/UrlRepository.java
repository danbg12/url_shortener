package com.url.shortener.repository;

import com.url.shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    @Modifying
    @Query(value = "DELETE FROM urls WHERE expires_at < CURRENT_TIMESTAMP RETURNING url_hash_code", nativeQuery = true)
    List<String> deleteExpiredUrlsAndReturnHashCodes();
}
