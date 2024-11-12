package com.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.spring.client.annotation.Deployment;

@SpringBootApplication
@Deployment(resources = "classpath:processes/*.bpmn")
public class Camunda8AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(Camunda8AppApplication.class, args);
	}

}
