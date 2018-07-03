package net.xeraa.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BackendApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	// Inject the required headers to keep track of requests
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.requestMatchers(EndpointRequest.to("health")).permitAll()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.antMatchers("/**").permitAll()
				.and()
				.httpBasic();
	}

}
