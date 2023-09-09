package com.apostolis.spms.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name",nullable = false)    
    private String name;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="price",nullable = false)
    private double price;
}
