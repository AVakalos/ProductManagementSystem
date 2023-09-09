package com.apostolis.spms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apostolis.spms.Exception.ResourceNotFoundException;
import com.apostolis.spms.model.CustomHttpResponse;
import com.apostolis.spms.model.Product;
import com.apostolis.spms.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/")
public class ProductController {
    @Autowired
	private ProductRepository productRepository;

    // Get all products in pages
    @GetMapping("/products")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size){
        try{
            Pageable paging = PageRequest.of(page, size);                                            
            Page<Product> retrievedProductsPage = productRepository.findAll(paging);
            List<Product> retrievedProducts = retrievedProductsPage.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("products", retrievedProducts);
            response.put("currentPage", retrievedProductsPage.getNumber()+1);
            response.put("totalItems", retrievedProductsPage.getTotalElements());
            response.put("totalPages", retrievedProductsPage.getTotalPages());
            return CustomHttpResponse.generateRespose("Retrieve products in pages", HttpStatus.OK, response);
        }catch(Exception e){
             return CustomHttpResponse.generateRespose(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Can not retrieve products.");
        }
    }

    // Create product 
    @PostMapping("/products")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> createProduct(@RequestBody Product product){
        try{
            Product savedProduct = productRepository.save(product);
            return CustomHttpResponse.generateRespose("Saved product", HttpStatus.CREATED, savedProduct);
        }catch(Exception e){
            return CustomHttpResponse.generateRespose(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Product not saved.");
        }
    }

    // Get product by id
    @GetMapping("/products/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try{
            Product retievedProduct = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not exist with id: "+id));
            return CustomHttpResponse.generateRespose("Retrieved product with id "+id, HttpStatus.OK, retievedProduct);
            
        }catch(ResourceNotFoundException r){
            return CustomHttpResponse.generateRespose(r.getMessage(), HttpStatus.NOT_FOUND, "");
        }
    }

    // Update product
    @PutMapping("/products/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody Product productDetails){
        try{
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id: "+id));
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());  
            Product updatedProduct = productRepository.save(product); 
            return CustomHttpResponse.generateRespose("Product with id "+id+" updated", HttpStatus.OK, updatedProduct);

        }catch(ResourceNotFoundException r){
            return CustomHttpResponse.generateRespose(r.getMessage(), HttpStatus.NOT_FOUND, "Can not update the product.");
        }
    }

    // Delete product
    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){
        try{
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id: "+id));

            productRepository.delete(product);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted",Boolean.TRUE);
            return CustomHttpResponse.generateRespose("Product with id: "+id+" deleted", HttpStatus.OK, response);

        }catch(ResourceNotFoundException r){
            return CustomHttpResponse.generateRespose(r.getMessage(), HttpStatus.NOT_FOUND, "Can not delete the product.");
        }
    }
}