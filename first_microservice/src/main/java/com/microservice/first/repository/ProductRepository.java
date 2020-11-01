package com.microservice.first.repository;


import com.microservice.first.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findProductByUrl(String url);
    void deleteByUrl(String url);
}
