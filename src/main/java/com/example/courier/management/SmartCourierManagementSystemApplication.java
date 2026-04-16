package com.example.courier.management;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SmartCourierManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCourierManagementSystemApplication.class, args);
	}

    @Bean
    public ModelMapper modelMapper(){ //For convert Object to Another Class
        return new ModelMapper();
    }

}
