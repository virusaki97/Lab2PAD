package com.microservice.first.service;

import com.microservice.first.models.Product;
import com.microservice.first.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByUrl(String url) {
        Product product = productRepository.findProductByUrl(url);

        return product;
    }

    public void createProduct(String name, String description, String price, String url) {
        if(getProductByUrl(url) == null) {
            Product product = new Product(null,
                    name.replaceAll(" ", ""),
                    description.substring(0, !description.contains("  ") ? description.length() : description.indexOf("  ")),
                    Integer.parseInt(price.replace(" ", "")),
                    url.replace(" ", ""));
            productRepository.save(product);
        }
    }

    public void updateProduct(String name, String description, String price, String url, String newUrl) {
        Product product = getProductByUrl(url.replaceAll(" ", ""));
        if(product != null) {

            product.setName(name.replaceAll(" ", ""));
            product.setDescription(description.substring(0, !description.contains("  ") ? description.length() : description.indexOf("  ")));
            product.setPrice(Integer.parseInt(price.replaceAll(" ", "")));
            product.setUrl(newUrl.replaceAll(" ", ""));

            productRepository.save(product);
        }
    }

    public void deleteProduct(String url) {
        productRepository.deleteByUrl(url);
    }
}
