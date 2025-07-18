package com.url.shortener.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface HashRepository {
    @Query(value = "SELECT NEXTVAL('unique_numbers_seq') AS unique_number FROM generate_series(1, :count)",
            nativeQuery = true)
    List<Long> getUniqueNumbers(@Param("count") Integer count);

    @Modifying
    @Transactional(rollbackFor = SQLException.class)
    @Query(value = "INSERT INTO hash (url_hash_code) SELECT UNNEST(:values::TEXT[])", nativeQuery = true)
    void save(@Param("values") String[] values);

    @Modifying
    @Transactional(rollbackFor = SQLException.class)
    @Query(value = "DELETE FROM hash WHERE url_hash_code IN " +
            "(SELECT url_hash_code FROM hash ORDER BY RANDOM() LIMIT :count) " +
            "RETURNING url_hash_code", nativeQuery = true)
    List<String> getHashBatch(@Param("count") Integer count );
}
