package com.microservice.second.repository;


import com.microservice.second.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findProductByUrl(String url);
    void deleteByUrl(String url);
}
