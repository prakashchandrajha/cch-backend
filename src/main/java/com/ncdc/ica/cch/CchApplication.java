package com.ncdc.ica.cch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import jakarta.servlet.MultipartConfigElement;

@SpringBootApplication
public class CchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CchApplication.class, args);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		// We configure the servlet multipart limits explicitly. If needed, update to a higher value.
		return new MultipartConfigElement("", 104857600, 104857600, 0);
	}

}
