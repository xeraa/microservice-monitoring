package net.xeraa.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class FrontendApplication {

	private static final Logger log = Logger.getLogger(FrontendApplication.class.getName());

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
		SpringApplication.run(FrontendApplication.class, args);
	}

	@RequestMapping("/good")
	public String good(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@RequestMapping("/bad")
	public String bad() {
		throw new RuntimeException("My bad, something went wrong...");
	}

	@RequestMapping("/call")
	public String callHome() throws InterruptedException {
		log.log(Level.INFO, "Calling home");
		Thread.sleep(this.random.nextInt(2000));
		return restTemplate.getForObject("http://backend.xeraa.wtf:80/home", String.class);
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
