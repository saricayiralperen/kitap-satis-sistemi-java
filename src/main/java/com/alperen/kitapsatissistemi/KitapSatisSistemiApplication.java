package com.alperen.kitapsatissistemi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Alperen Sarıçayır Kitap Satış Sistemi
 * Java Spring Boot uygulaması
 * .NET C# projesinden dönüştürülmüştür
 */
@SpringBootApplication
@EntityScan("com.alperen.kitapsatissistemi.entity")
@EnableJpaRepositories("com.alperen.kitapsatissistemi.repository")
public class KitapSatisSistemiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitapSatisSistemiApplication.class, args);
    }

}
