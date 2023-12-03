package br.com.m3tech.AppGas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppGasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppGasApplication.class, args);

	}

}
