package com.ansley.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySecondApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySecondApplication.class, args);
		
		System.out.println("Hello");
	}

}
