package com.marakogr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MarakogrKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarakogrKafkaApplication.class, args);
	}


}
