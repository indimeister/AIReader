package com.indimeister.ai.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.indimeister"})
@EntityScan(basePackages = {"com.indimeister.ai.reader.domain"})
@EnableJpaRepositories(basePackages = "com.indimeister.ai.reader.repository")
@EnableAutoConfiguration
public class AiReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiReaderApplication.class, args);
	}
}
