package com.rm.mynotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNotesApplication.class, args);
	}

}
