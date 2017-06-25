package net.xeraa.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }

    // Inject the required headers to keep track of requests
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
