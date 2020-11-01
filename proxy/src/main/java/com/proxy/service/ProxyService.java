package com.proxy.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.proxy.cache.MemoryCache;
import com.proxy.load_balancer.RandomLoadBalancer;
import com.proxy.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProxyService {
    private final MemoryCache<String, Product> memoryCache = new MemoryCache<>(500, 600);
    private final RandomLoadBalancer loadBalancer = new RandomLoadBalancer(
            new ArrayList<>(
                    List.of(
                            "http://localhost:1111/",
                            "http://localhost:1122/"
                    )
            )
    );

    public Object getAllProducts() {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get(loadBalancer.getUrl() + "product/list")
                    .asString();

            if (!(response.getBody() == null)) {
                return response.getBody();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void httpCreate(String name, String description, int price, String url, String microserviceUrl) {
        Unirest.setTimeouts(0, 0);
        try {
            Unirest.put(microserviceUrl + "product/create")
                    .queryString("name", name)
                    .queryString("description", description)
                    .queryString("price", price)
                    .queryString("url", url)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void createProduct(String name, String description, int price, String url) {
        String currMicroserviceUrl = loadBalancer.getUrl();
        httpCreate(name, description, price, url, currMicroserviceUrl);

        ArrayList<String> list = loadBalancer.getUrlWithout(currMicroserviceUrl);
        for (String microUrl : list) {
            new Thread(() -> httpCreate(name, description, price, url, microUrl)).start();
        }

        try {
            HttpResponse<String> readResponse = Unirest.get(currMicroserviceUrl + "product/read")
                    .queryString("url", url)
                    .asString();

            if (memoryCache.get(url) == null) {
                putProductInCache(readResponse.getBody());
            } else {
                System.out.println("Product already exists.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    public Object getEmployerByUrl(String url) {
        if (memoryCache.containsKey(url)) {
            System.out.println("Read product from cache.");
            return memoryCache.get(url);
        } else {
            System.out.println("Read product from database.");
            Unirest.setTimeouts(0, 0);
            try {
                HttpResponse<String> response = Unirest.get(loadBalancer.getUrl() + "product/read")
                        .queryString("url", url)
                        .asString();

                if (response.getBody() != null) {
                    putProductInCache(response.getBody());

                    return response.getBody();
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Null data");
        return null;
    }

    private void httpUpdate(String name, String description, int price, String url, String newUrl, String microserviceUrl) {
        Unirest.setTimeouts(0, 0);
        try {
            Unirest.put(microserviceUrl + "product/update")
                    .queryString("name", name)
                    .queryString("description", description)
                    .queryString("price", price)
                    .queryString("url", url)
                    .queryString("newUrl", newUrl)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(String name, String description, int price, String url, String newUrl) {
        String currMicroserviceUrl = loadBalancer.getUrl();
        httpUpdate(name, description, price, url, newUrl, currMicroserviceUrl);

        ArrayList<String> list = loadBalancer.getUrlWithout(currMicroserviceUrl);
        for (String microUrl : list) {
            new Thread(() -> httpUpdate(name, description, price, url, newUrl, microUrl)).start();
        }

        try {
            HttpResponse<String> readResponse = Unirest.get(currMicroserviceUrl + "product/read")
                    .queryString("url", newUrl)
                    .asString();

            if (!readResponse.getBody().isEmpty()) {
                removeProductFromCache(url);
                putProductInCache(readResponse.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    private void httpDelete(String url, String microserviceUrl) {
        Unirest.setTimeouts(0, 0);
        try {
            Unirest.delete(microserviceUrl + "product/delete")
                    .queryString("url", url)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(String url) {
        String currMicroserviceUrl = loadBalancer.getUrl();
        httpDelete(url, currMicroserviceUrl);

        ArrayList<String> list = loadBalancer.getUrlWithout(currMicroserviceUrl);
        for (String microUrl : list) {
            new Thread(() -> httpDelete(url, microUrl)).start();
        }

        removeProductFromCache(url);
    }

    private void putProductInCache(String productJson) {
        System.out.println("Put product in cache.");
        Product product = Product.fromJson(productJson);

        String key = product.getUrl();
        memoryCache.put(key, product);

    }

    private void removeProductFromCache(String key) {
        System.out.println("Remove product from cache.");
        memoryCache.remove(key);
    }
}
