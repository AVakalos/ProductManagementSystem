package com.apostolis.spms.service;

import org.springframework.data.domain.Page;

import com.apostolis.spms.model.Product;

public interface ProductService {
    public Page<Product> getProductsInPage(int page, int size);
    public Product createProduct(Product product);
    public Product getProductById(Long id);
    public Product updateProduct(Long id, Product productDetails);
    public void deleteProduct(Long id);
}
