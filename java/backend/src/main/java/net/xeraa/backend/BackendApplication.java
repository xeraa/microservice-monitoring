package net.xeraa.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class BackendApplication {

	private static final Logger log = Logger.getLogger(BackendApplication.class.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Random random;

	// Inject the required headers to keep track of requests
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@RequestMapping("/home")
	public String home() {
		log.log(Level.INFO, "You called home");
		return "Called home!";
	}

	@RequestMapping("/call-bad")
	public String callBad() throws InterruptedException {
		log.log(Level.INFO, "Calling bad");
		Thread.sleep(this.random.nextInt(2000));
		return restTemplate.getForObject("http://frontend.xeraa.wtf:80/bad", String.class);
	}

	@RequestMapping("/call-nested")
	public String callNested() throws InterruptedException {
		log.log(Level.INFO, "Calling call");
		Thread.sleep(this.random.nextInt(1000));
		return restTemplate.getForObject("http://frontend.xeraa.wtf:80/call", String.class);
	}

}
