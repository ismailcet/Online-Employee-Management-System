package com.ismailcet.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication() //exclude = {SecurityAutoConfiguration.class }
public class OnlineEmployeeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineEmployeeManagementSystemApplication.class, args);
	}
}
