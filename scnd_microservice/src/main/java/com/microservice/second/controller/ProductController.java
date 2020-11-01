package com.microservice.second.controller;

import com.microservice.second.models.Product;
import com.microservice.second.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Product> getProductList() {
        List<Product> list = productService.getAllProducts();
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public @ResponseBody
    void addProduct(@ModelAttribute(name = "name") String name,
                    @ModelAttribute(name = "description") String description,
                    @ModelAttribute(name = "price") String price,
                    @ModelAttribute(name = "url") String url) {

        productService.createProduct(name, description, price, url);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public @ResponseBody
    Product readProduct(@ModelAttribute(name = "url") String url) {

        return productService.getProductByUrl(url);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody
    void updateProduct(@ModelAttribute(name = "name") String name,
                       @ModelAttribute(name = "description") String description,
                       @ModelAttribute(name = "price") String price,
                       @ModelAttribute(name = "url") String url,
                       @ModelAttribute(name = "newUrl") String newUrl) {

        productService.updateProduct(name, description, price, url, newUrl);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    void deleteProduct(@ModelAttribute(name = "url") String url) {

        productService.deleteProduct(url);
    }
}
