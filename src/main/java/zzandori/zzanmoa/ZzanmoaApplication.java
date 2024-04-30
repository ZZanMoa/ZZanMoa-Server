package zzandori.zzanmoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZzanmoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzanmoaApplication.class, args);
	}

}