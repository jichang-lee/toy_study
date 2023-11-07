package org.spring.testspring;

import org.spring.testspring.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class TestspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestspringApplication.class, args);
	}

}
