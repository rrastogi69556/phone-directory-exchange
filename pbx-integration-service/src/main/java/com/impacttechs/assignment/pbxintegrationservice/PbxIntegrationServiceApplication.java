package com.impacttechs.assignment.pbxintegrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PbxIntegrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PbxIntegrationServiceApplication.class, args);
	}

}
