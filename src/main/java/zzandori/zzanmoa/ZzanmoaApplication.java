package zzandori.zzanmoa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://3.39.103.75:8080", description = "Generated server url")})
@SpringBootApplication
public class ZzanmoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzanmoaApplication.class, args);
	}

}