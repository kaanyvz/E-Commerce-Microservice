package com.ky.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = {
        "com.ky.productservice",
        "com.ky.amqp"
})
@EnableDiscoveryClient
public class ProductServiceApplication{


    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
