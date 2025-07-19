package com.url.shortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fake_hash")
public class Hash {
    @Id
    private Long id;
}
