package com.apostolis.spms.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apostolis.spms.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Page<Product> findAll(Pageable pageable);
}
