package com.impacttechs.assignment.phonebookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhonebookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhonebookServiceApplication.class, args);
	}

}
