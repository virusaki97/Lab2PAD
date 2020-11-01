package com.proxy.controller;

import com.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/product")
public class ProxyController {
    @Autowired
    ProxyService proxyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object getEmployeeList() {

        return proxyService.getAllProducts();
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public void createEmployee(@ModelAttribute(name = "name") String name,
                               @ModelAttribute(name = "description") String description,
                               @ModelAttribute(name = "price") int price,
                               @ModelAttribute(name = "url") String url) {

        proxyService.createProduct(name, description, price, url);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public Object readEmployee(@ModelAttribute(name = "url") String url) {

        return proxyService.getEmployerByUrl(url);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateEmployee(@ModelAttribute(name = "name") String name,
                               @ModelAttribute(name = "description") String description,
                               @ModelAttribute(name = "price") int price,
                               @ModelAttribute(name = "url") String url,
                               @ModelAttribute(name = "newUrl") String newUrl) {

        proxyService.updateProduct(name, description, price, url, newUrl);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteEmployee(@ModelAttribute(name = "url") String url) {

        proxyService.deleteProduct(url);
    }
}
