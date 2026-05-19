package com.AppRH.AppRH;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.AppRH.AppRH")
@EnableCaching
public class AppRhApplication {

	public static void main(String[] args) throws SQLException {
		System.out.println("TESTE");
		SpringApplication.run(AppRhApplication.class, args);
	}
}
