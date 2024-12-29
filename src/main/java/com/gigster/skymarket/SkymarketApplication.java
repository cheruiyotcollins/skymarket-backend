package com.gigster.skymarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SkymarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkymarketApplication.class, args);
	}

}
