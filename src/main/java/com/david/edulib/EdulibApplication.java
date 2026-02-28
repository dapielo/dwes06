package com.david.edulib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EdulibApplication {
	/**
	 * 1 - Entidades
	 * 2 - DTOs
	 * 3 - mappers
	 * 3 - Repositories
	 * 4 - services
	 * 5 - controller
	 * dataInit, excepciones, controlleradvise, APPLICATION.PROPERTIES
	 */

	public static void main(String[] args) {
		SpringApplication.run(EdulibApplication.class, args);
	}

}
