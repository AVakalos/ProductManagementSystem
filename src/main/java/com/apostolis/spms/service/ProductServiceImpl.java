package com.apostolis.spms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.apostolis.spms.Exception.ResourceNotFoundException;
import com.apostolis.spms.model.Product;
import com.apostolis.spms.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
    
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Page<Product> getProductsInPage(int page, int size){
        Pageable paging = PageRequest.of(page, size);                                            
        return productRepository.findAll(paging);
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not exist with id: "+id));
    }

    public Product updateProduct(Long id, Product productDetails){
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id: "+id));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());  
        return productRepository.save(product); 
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id: "+id));
        productRepository.delete(product);
    }
}
