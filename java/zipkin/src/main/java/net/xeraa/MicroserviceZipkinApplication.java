package net.xeraa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class MicroserviceZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZipkinApplication.class, args);
	}
}
