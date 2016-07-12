package com.baltimorecrime.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baltimorecrime.core.mapper")
public class BaltimoreCrimeCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaltimoreCrimeCoreApplication.class, args);
	}
}
