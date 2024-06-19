package kz.project.techway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechwayApplication.class, args);
	}

}
